from flask import Flask, render_template, request
import requests
import logging

import helpers

app = Flask(__name__)
# Configure Flask logging
app.logger.setLevel(logging.INFO)  # Set log level to INFO
handler = logging.FileHandler('app.log')  # Log to a file
app.logger.addHandler(handler)


@app.route('/')
def index():
    return render_template('index.html')


@app.route('/fetch-weather', methods=['GET'])
def fetch_weather():
    url = 'http://localhost:8080/api/weather-data'

    city_location = request.args.get('city')
    country_location = request.args.get('country')
    # TODO: request start date and end date from user
    start_date = "2024-08-20 09:00:00"  # request.args.get('start_date')
    end_date = "2024-08-31 09:00:00"  # request.args.get('end_date')

    params = {'city': city_location,
              'country': country_location,
              'start': start_date,
              'end': end_date}
    
    response = requests.get(url, params=params)
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
    temperature_graph_html = helpers.create_html_graph(x_data=data["tomorrow-weather"]['timestamp'],
                                                       y_data=data['tomorrow-weather']['temperature'],
                                                       title="Temperature", y_label='Temperature (°C)')
    humidity_graph_html = helpers.create_html_graph(x_data=data["tomorrow-weather"]["timestamp"],
                                                    y_data=data['tomorrow-weather']['humidity'],
                                                    title="Humidity", y_label='Humidity (%)')
    # temperature_graph_html = helpers.create_html_graph(x_data=data["tomorrow-weather"]['timestamp'],
    #                                                    y_data=[data['accu-weather']['temperature'],
    #                                                            data['open-weather']['temperature'],
    #                                                            data['tomorrow-weather']['temperature']],
    #                                                    title="Temperature", y_label='Temperature (°C)')
    # humidity_graph_html = helpers.create_html_graph(x_data=data["tomorrow-weather"]["timestamp"],
    #                                                 y_data=[data['accu-weather']['humidity'],
    #                                                         data['open-weather']['humidity'],
    #                                                         data['tomorrow-weather']['humidity']],
    #                                                 title="Humidity", y_label='Humidity (%)')

    return render_template('comparison.html',
                           temperature_plot=temperature_graph_html,
                           humidity_plot=humidity_graph_html)


@app.route('/trigger', methods=['POST'])
def trigger():
    url = 'http://localhost:8080/api/trigger'

    headers = {"Content-Type": "application/json; charset=UTF-8"}
    params = {'city': request.values.get('city'),
              'country': request.values.get('country')}

    response = requests.post(url, json=params, headers=headers)
    if response.status_code >= 400:
        pass

    return render_template('index.html')


if __name__ == '__main__':
    app.run(debug=True)
