from flask import Flask, render_template, request
import requests

import helpers

app = Flask(__name__)


@app.route('/')
def index():
    return render_template('index.html')


@app.route('/fetch-weather', methods=['GET'])
def fetch_weather():
    url = 'http://localhost:8080/api/weather-data'

    city_location = request.args.get('city')
    country_location = request.args.get('country')

    location = {'city': city_location, 'country': country_location, 'latitude': 32.0, 'longitude': 32.0}
    response = requests.get(url, json=location)
    if response.status_code >= 400:
        return render_template('index.html')

    data = helpers.clean_data(response.json())

    ## visualization
    # create temperature plot
    temperature_graph_html = helpers.create_html_graph(data["Tomorrow"]['timestamp'],
                                                       [data['Accu-Weather']['temperature'],
                                                        data['Open-Weather']['temperature'],
                                                        data['Tomorrow']['temperature']],
                                                       "Temperature", 'Temperature (°C)')
    humidity_graph_html = helpers.create_html_graph(data["Tomorrow"]["timestamp"],
                                                    [data['Accu-Weather']['humidity'],
                                                     data['Open-Weather']['humidity'],
                                                     data['Tomorrow']['humidity']],
                                                    "Humidity", 'Humidity (%)',)

    return render_template('comparison.html',
                           temperature_plot=temperature_graph_html,
                           humidity_plot=humidity_graph_html)


if __name__ == '__main__':
    app.run(debug=True)
