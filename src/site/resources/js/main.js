var map = L.map('map').setView([49.8, 15], 8);
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: 'Map data © <a href="http://openstreetmap.org">OpenStreetMap</a> contributors',
    maxZoom: 18
}).addTo(map);

var renderOpeningTimes = function (openingTimes) {
    var names = {
        'monday': 'Po',
        'tuesday': 'Út',
        'wednesday': 'St',
        'thursday': 'Čt',
        'friday': 'Pá',
        'saturday': 'So',
        'sunday': 'Ne'
    };
    var result = '';
    for (var key in names) {
        var czName = names[key];
        var times = openingTimes[key];
        if (times == null) {
            times = "-";
        }
        result = result + czName + ": " + times + "<br>";
    }
    return result;
};

var renderLegend = function (markers, cluster, knownTypes) {

    var addType = function (div, type) {

        var typeLine = document.createElement('div');
        var checkbox = $(document.createElement('input')).attr({
            type: 'checkbox',
            checked: 'checked'
        });

        $(checkbox).change(function () {
            var checked = $(this).is(':checked');
            markers.forEach(function (marker) {
                if (marker.rawData.type == type) {
                    if (checked) {
                        cluster.addLayer(marker);
                    } else {
                        cluster.removeLayer(marker);
                    }
                }
            });
        });

        $(typeLine).append(checkbox);
        $(typeLine).append("&nbsp;" + "<i class='fa fa-"+getIconName(type)+"'></i>" + "&nbsp;" + type);
        $(div).append(typeLine);

//        this._div.innerHTML = this._div.innerHTML + "<input type='checkbox' checked='checked'/>" + key + "<br>"
    };

    var info = L.control({position: 'topright'});
    info.onAdd = function (map) {
        this._div = L.DomUtil.create('div', 'info'); // create a div with a class "info"
        this._div.innerHTML = '<h4>Typy Czechpointů:</h4>';
        for (var key in knownTypes) {
            addType(this._div, key);
        }
        return this._div;
    };
    info.addTo(map);

};

var renderContacts = function (contacts) {
    var renderLink = function(link, protocol) {
       var href = link;
       if(protocol != null) {
          href = protocol + ":" + link;
       }
       var label = link.replace("http://", "").replace("https://", "");
       if(label.length > 30) {
            label = label.substring(0,30) + "...";
       }
       var link = $(document.createElement('a')).attr('href', href).text(label);
       return link;
    }

    var table = $(document.createElement('table'));
    contacts.forEach(function(contact){
        if (contact.href != null) {
            table.append(
               $(document.createElement('tr')).append([
                    $(document.createElement('td')).append(contact.label + ": "),
                    $(document.createElement('td')).append(renderLink(contact.href, contact.protocol))
                ])
            );
        }
    });
    return table;
};


var constructPopupContent = function (item) {

    var content = $(document.createElement('div'));

    if (item.name != null) {
        content.append("<h2>" + item.name + " <small> - " + item.type + "</small></h2>");
    } else {
        content.append("<h2>" + item.type + "</h2>");
    }

    var street = item.street;

    if (street.match(/^\d+$/)) {
        street = item.city + " " + item.street;
    }

    content.append("<p>" + street + "<br>" + item.zip + " " + item.city + "</p>");

    var contacts = [];
    contacts.push({'label': 'Web', 'href': item.www});
    contacts.push({'label':'E-mail', 'href':item.email, 'protocol':'mailto'});
    contacts.push({'label':'Telefon', 'href': item.phone, 'protocol':'tel'});


    if (item.type == 'Česká pošta') {
        var url = "https://www.postaonline.cz/vyhledani-psc?p_p_id=psc_WAR_pcpvpp&p_p_lifecycle=1&p_p_state=normal&p_p_mode=view&p_p_col_id=column-1&p_p_col_count=1&_psc_WAR_pcpvpp_struts.portlet.action=%2Fview%2FdetailPost&_psc_WAR_pcpvpp_struts.portlet.mode=view&_psc_WAR_pcpvpp_zipCode=" + item.zip + "&_psc_WAR_pcpvpp_form.zip=&_psc_WAR_pcpvpp_form.city="
        contacts.push({'label': 'Web', 'href': url});
    }

    content.append(renderContacts(contacts));

    if (item.openingTimes != null) {
        content.append("<h4>Otevírací doba:</h4> " + renderOpeningTimes(item.openingTimes) + "</p>");
    }

    content.append(" <p>Geolokační data: " + item.location.licence + "</p>");
    return content.html();
};

var getIconName = function(type) {
 var mapping = {
  'Banka':'university',
  'Úřad':'home',
  'Notář':'gavel',
  'Česká pošta':'paper-plane',
  'Zastupitelský úřad ČR':'life-ring',
  'Hospodářská komora':'shield',
  }
  return mapping[type];  
}
  

var popupEventFunction = function(marker) {
    marker.unbindPopup();
    marker.bindPopup(constructPopupContent(marker.rawData));
    $(location).attr('hash', marker.rawData.id);
    marker.openPopup();
}

var getIcon = function(item) {
  var markerIcon = L.AwesomeMarkers.icon({
    icon: getIconName(item.type),
    markerColor: 'darkblue',
    prefix: 'fa'
  });

  return markerIcon;
}


$.get("czechpoints.json", function(data) {
    var knownTypes = {};
    var cluster = new L.MarkerClusterGroup({maxClusterRadius:50});
    var allMarkers = [];
    data.forEach(function (item) {
        knownTypes[item.type] = true;
        if (item.location !== null) {

            var marker = L.marker([item.location.lat, item.location.lon], {icon: getIcon(item)});
            marker.rawData = item;
            marker.on('click', function(e) {
                popupEventFunction(this);
            });
            allMarkers.push(marker);
        } else {
            //console.log("Czechpoint bez adresy:" + JSON.stringify(item));
        }
    });

    cluster.addLayers(allMarkers);
    map.addLayer(cluster);
    renderLegend(allMarkers, cluster, knownTypes);

    var hash = $(location).attr('hash');
    if(typeof hash != "undefined" && hash.length > 1) {
        hash = hash.substr(1);
        var filtered = allMarkers.filter(function(item){return item.rawData.id == hash});
        console.log(filtered);
        filtered.forEach(function(item){
            cluster.zoomToShowLayer(item, function() {
                popupEventFunction(item);
            })
        });
    }
});