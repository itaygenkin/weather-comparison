from flask import Flask, render_template, request
import requests
import logging

import helpers

app = Flask(__name__)
# Configure Flask logging
formatter = logging.Formatter("[%(asctime)s] %(levelname)s in %(module)s: %(message)s", datefmt="%Y-%m-%d %H:%M:%S")
handler = logging.StreamHandler()
handler.setFormatter(formatter)

app.logger.addHandler(handler)
app.logger.setLevel(logging.INFO)


@app.route('/')
def index():
    default_time = helpers.get_default_time()
    return render_template('index.html',
                           default_start_time=default_time[0],
                           default_end_time=default_time[1])


@app.route('/fetch-weather', methods=['GET'])
def fetch_weather():
    url = 'http://localhost:8080/api/weather-data'

    city_location = request.args.get('city')
    country_location = request.args.get('country')

    start_date = request.args.get('start_date')
    end_date = request.args.get('end_date')

    params = {'city': city_location,
              'country': country_location,
              'start': start_date,
              'end': end_date}
    
    response = requests.get(url, params=params)
    app.logger.info(f"fetch_weather:\tparams({params});\tresponse code: {response.status_code}")

    if response.status_code >= 400:
        app.logger.error(response.text)
        return render_template('index.html')
    elif helpers.is_empty_response(response.json()):
        app.logger.error("empty response")
        return render_template('index.html')

    app.logger.info(response.status_code)
    data = helpers.clean_data(response.json())
    app.logger.info(data)

    ## visualization
    # create temperature plot
    temperature_html_graph = helpers.html_graph(x_data=data["tomorrow-weather"]['timestamp'],
                                                y_data=[data['tomorrow-weather']['temperature'],
                                                        data['open-weather']['temperature']],
                                                y_label='Temperature (°C)', value_name='Temperature')

    humidity_html_graph = helpers.html_graph(x_data=data["tomorrow-weather"]['timestamp'],
                                             y_data=[data['tomorrow-weather']['humidity'],
                                                     data['open-weather']['humidity']],
                                             y_label='Humidity (%)', value_name='Humidity')

    return render_template('comparison.html',
                           temperature_plot=temperature_html_graph,
                           humidity_plot=humidity_html_graph)


@app.route('/trigger', methods=['POST'])
def trigger():
    url = 'http://localhost:8080/api/trigger'

    headers = {"Content-Type": "application/json; charset=UTF-8"}
    params = {'city': request.values.get('city'),
              'country': request.values.get('country')}

    response = requests.put(url, json=params, headers=headers)
    app.logger.info(f"trigger:\tparams({params});\tresponse code: {response.status_code}")

    if response.status_code >= 400:
        logging.info(f"trigger failed, status code: {response.status_code}")

    default_time = helpers.get_default_time()
    return render_template('index.html',
                           default_start_time=default_time[0],
                           default_end_time=default_time[1])


def fetch_cities():
    try:
        response = requests.get("http://localhost:8080/api/cities")
        cities = response.json()
        logging.info(f"Cities fetched:\t{cities}")
        return helpers.convert_location_to_city_format(cities)
    except Exception as e:
        logging.error(f"Failed to fetch cities: {e}")
        return []


@app.route('/map')
def show_map():
    cities = fetch_cities()
    return render_template('map.html', cities=cities)


if __name__ == '__main__':
    app.run(debug=True)
