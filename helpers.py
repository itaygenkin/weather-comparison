from datetime import datetime

import plotly.express as px


def title_pretty_print(city: str, country: str) -> str:
    """
    convert the city and country names into a pretty formatted string (Camel Case)
    :param city: a city name (str)
    :param country: a country name (str)
    :return: formatted string
    """
    pretty_city = city.replace('-', ' ').split()
    pretty_country = country.replace('-', ' ').split()

    pretty_city = ' '.join([x[0].upper() + x[1:] for x in pretty_city])
    pretty_country = ' '.join([x[0].upper() + x[1:] for x in pretty_country])
    return f"{pretty_city}, {pretty_country}"


def shorten_timestamp(time: str) -> str:
    """
    reformat timestamp from a response entity
    :param time: a timestamp from a response entity
    :return: a formatted timestamp
    """
    return time[:10] + " " + time[11:19]


def clean_sample(sample: dict) -> dict:
    """
    keep relevant data of a sample
    :param sample: a sample data represented as a dict
    :return: the sample data with relevant data only
    """
    return {'timestamp': shorten_timestamp(sample['time']),
            'temperature': sample['temperature'],
            'humidity': sample['humidity']}


def get_clean_samples(json: dict) -> dict:
    """
    clean the samples data for each list of sample data
    :param json: weather packet data given from a response entity
    :return: 3 lists in a dictionary
    """
    wl1 = list(map(clean_sample, json['list1']['samples']))
    wl2 = list(map(clean_sample, json['list2']['samples']))
    wl3 = list(map(clean_sample, json['list3']['samples']))
    return {json['list1']['source']: wl1,
            json['list2']['source']: wl2,
            json['list3']['source']: wl3}


def get_default_dict_from_sources(json: dict) -> dict:
    sources = [json['list1']['source'], json['list2']['source'], json['list3']['source']]
    return {
        source: {
            "temperature": [],
            "humidity": [],
            "timestamp": []
        }
        for source in sources
    }


def clean_data(json: dict) -> dict:
    data = get_clean_samples(json)
    my_dict = get_default_dict_from_sources(json)

    for key in my_dict.keys():
        for item in data[key]:
            my_dict[key]['temperature'].append(item['temperature'])
            my_dict[key]['humidity'].append(item['humidity'])
            my_dict[key]['timestamp'].append(item['timestamp'])
    return my_dict


def create_html_graph(x_data, y_data, title: str, y_label: str):
    figure = px.line(x=x_data, y=y_data)

    figure.update_layout(title=title, xaxis_title='Timestamp', yaxis_title=y_label)
    figure.data[0].name = 'Accu-Weather'
    figure.data[1].name = 'Open-Weather'
    figure.data[2].name = 'Tomorrow'
    return figure.to_html(full_html=True)


def is_empty_response(response_json):
    for key in response_json.keys():
        if response_json[key]['samples']:
            return False
    return True


def get_default_time():
    now = datetime.now()
    default_start_time = now.replace(hour=0, minute=0).strftime('%Y-%m-%dT%H:%M')
    default_end_time = now.strftime('%Y-%m-%dT%H:%M')
    return default_start_time, default_end_time