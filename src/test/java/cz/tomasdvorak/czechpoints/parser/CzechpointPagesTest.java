package cz.tomasdvorak.czechpoints.parser;

import cz.tomasdvorak.czechpoints.TestUtils;
import cz.tomasdvorak.czechpoints.dto.DataPage;
import cz.tomasdvorak.czechpoints.providers.CzechpointPages;
import cz.tomasdvorak.czechpoints.providers.PagesProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class CzechpointPagesTest {


    private PagesProvider service;
    @Before
    public void setUp() throws Exception {
        this.service = new CzechpointPages() {
            @Override
            protected String getHomepageContent() throws Exception {
                return TestUtils.getText("/czechpointhomepage.example");
            }
        };

    }

    @Test
    public void getKnownPages() throws Exception {
        final List<DataPage> pages = service.getKnownPages();
        Assert.assertEquals(71, pages.size());
    }
}