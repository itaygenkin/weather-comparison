/**
 * convert a city name and a country name to pretty formatted string (Camel Case)
 * @param city - a city name
 * @param country - a country name
 * @returns a pretty formatted string
 */
export function convertTitleToCamelCase(city: string, country: string): string {
    let camelCity: string[] = city.trim().replace('-', ' ').split(' ');
    let camelCountry: string[] = country.trim().replace('-', ' ').split(' ');

    const toUpperCaseFirstLetter = (s: string): string => s.slice(0, 1).toUpperCase() + s.slice(1);
    return [camelCity.map(toUpperCaseFirstLetter).join(' '), camelCountry.map(toUpperCaseFirstLetter).join(' ')]
        .filter((s: string) => s.length > 0).join(', ');
}

/**
 * reformat timestamp from a response entity
 * @param time - a timestamp from a response entity
 * @returns a formatted timestamp
 */
export function shortenTimestamp(time: string): string {
    return time.slice(0, 10) + time.slice(11, 19);
}

/**
 * keep relevant data of a sample
 * @param sample - a sample of data represented as a js object
 * @returns
 */
function cleanSample(sample: object): object {
    return {
        'timestamp': shortenTimestamp(sample['time']),
        'temperature': sample['temperature'],
        'humidity': sample['humidity']
    };
}

/**
 * clean the data samples for each list of data sample
 * @param data - weather packet data given from a response entity
 * @returns a js object containing 3 lists
 */
export function cleanSamples(data: object): object {
    return {
        data['list1']['source']: data['list1']['samples'].map(cleanSample),
        data['list2']['source']: data['list2']['samples'].map(cleanSample),
        data['list3']['source']: data['list3']['samples'].map(cleanSample)
    };
}

function defaultSourcesObject(data: object): object {
    const sources = [data['list1']['source'], data['list2']['source'], data['list3']['source']]
    return {
        sources[0]: {"temperature": [], "humidity": [], "timestamp": []},
        sources[1]: {"temperature": [], "humidity": [], "timestamp": []},
        sources[2]: {"temperature": [], "humidity": [], "timestamp": []}
    };
}

export function cleanData(data: object): object {
    let cleanSamplesObject = cleanSamples(data);
    let myObject = defaultSourcesObject(data);

    for (let key in myObject){
        for (let item in cleanSamplesObject[key]){
            myObject[key]['temperature'].append(item['temperature'])
            myObject[key]['humidity'].append(item['humidity'])
            myObject[key]['timestamp'].append(item['timestamp'])
        }
    }

    return myObject;
}

// def create_html_graph(x_data, y_data, title: str, y_label: str):
//     figure = px.line(x=x_data, y=y_data)

//     figure.update_layout(title=title, xaxis_title='Timestamp', yaxis_title=y_label)
//     # figure.data[0].name = 'Accu-Weather'
//     # figure.data[1].name = 'Open-Weather'
//     figure.data[0].name = 'Tomorrow'
//     return figure.to_html(full_html=True)

export function createHtmlGraph(xData: object, yData: object, title: string, y_label: string) {

}