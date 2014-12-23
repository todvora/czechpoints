package cz.tomasdvorak.czechpoints.workflow;

import cz.tomasdvorak.czechpoints.coordinates.*;
import cz.tomasdvorak.czechpoints.cpost.CpostApiService;
import cz.tomasdvorak.czechpoints.cpost.CpostApiServiceImpl;
import cz.tomasdvorak.czechpoints.dto.Coordinates;
import cz.tomasdvorak.czechpoints.dto.Czechpoint;
import cz.tomasdvorak.czechpoints.dto.DataPage;
import cz.tomasdvorak.czechpoints.dto.PostApiData;
import cz.tomasdvorak.czechpoints.parser.CzechpointPageParser;
import cz.tomasdvorak.czechpoints.parser.PageParser;
import cz.tomasdvorak.czechpoints.persistence.JsonPersistenceService;
import cz.tomasdvorak.czechpoints.persistence.PersistenceService;
import cz.tomasdvorak.czechpoints.providers.CzechpointPages;
import cz.tomasdvorak.czechpoints.utils.IdentificatorUtils;

import java.io.IOException;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class Workflow {
    public static final String DATAFILE = "src/main/resources/czechpoints.json";

    private final PersistenceService persistenceService = new JsonPersistenceService(
            Paths.get(PersistenceService.class.getResource("/").getFile()).getParent().getParent().resolve(DATAFILE)
    );
    private final PageParser pageParser = new CzechpointPageParser();

    private final CpostApiService cpostApiService = new CpostApiServiceImpl();

   
    private final LocationService locationService = new CachingLocationService(
            new MasterValidatorLocationService(
                    new NominatimService(),
                    new SeznamCzLocationService()
            )
        , persistenceService);

    private final List<Czechpoint> toPersist = new ArrayList<>();

    public void start() throws Exception {
                getOverviewPages()                     // generate stream of all known czechpoint listing pages
                    .map(this::parsePages)         // readCzechpoints czechpoints from pages
                    .flatMap(Collection::stream)   // concat all lists of czechpoints to one stream
                    .map(this::addId)     // add computed ID (MD5 of name and address)
                    .map(this::addPostData)     // add czech post details, if applicable
                    .map(this::addLocation)        // add location
                    .forEach(this::persist);   // persist


    }

    private Czechpoint addId(final Czechpoint czechpoint) {
        try {
            String computedID = IdentificatorUtils.getId(czechpoint);
            czechpoint.setId(computedID);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return czechpoint;
    }


    private void persist(final Czechpoint czechpoint) {
        toPersist.add(czechpoint);
        try {
            persistenceService.writeAll(toPersist);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private List<Czechpoint> parsePages(final DataPage dataPage) {
        try {
            return pageParser.readCzechpoints(dataPage);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Czechpoint addPostData(final Czechpoint czechpoint) {
        if (CzechpointPages.CZECH_POST_TYPE.equals(czechpoint.getType())) {
            try {
                System.out.println("Calling cpost api for zip: " + czechpoint.getZip());
                final PostApiData apiData = cpostApiService.getByZip(czechpoint.getZip());
                if(apiData != null) {
                    czechpoint.setLocation(
                            new Coordinates(
                                    "© Česká pošta, https://b2c.cpost.cz/",
                                    apiData.getLatitude(),
                                    apiData.getLongitude(),
                                    apiData.getStreet() + ", " + apiData.getCommune()
                            ));

                    czechpoint.setPhone(apiData.getPhone());
                    czechpoint.setFax(apiData.getFax());
                    czechpoint.setEmail(apiData.getEmail());
                    czechpoint.setOpeningTimes(apiData.getOpeningTimes());
                }
            } catch (final Exception e) {
//                throw new RuntimeException(e);
                e.printStackTrace();
            }
        }
        return czechpoint;
    }

    private Czechpoint addLocation(final Czechpoint czechpoint) {
        try {
            if(czechpoint.getLocation() == null) {
                czechpoint.setLocation(locationService.getCoordinates(czechpoint));
            }
        } catch (final Exception e) {
            e.printStackTrace();
//            throw new RuntimeException(e);
        }
        return czechpoint;
    }

    private Stream<DataPage> getOverviewPages() throws Exception {
        return new CzechpointPages().getKnownPages().stream();
    }
}
