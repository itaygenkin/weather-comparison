import React from 'react';
import { Line } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend } from 'chart.js';

// Register chart components
ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend);

export function createChartComponent(x_data, y_datasets, title, y_label){
    // chart data
    const data = {
        labels: x_data,
        datasets: [
            {
                label: y_datasets[0]['label'],
                data: y_datasets[0]['data'],
                fill: false,
                borderColor: 'rgb(192, 48, 48)',
                tension: 0.1
            },
            {
                label: y_datasets[1]['label'],
                data: y_datasets[1]['data'],
                fill: false,
                borderColor: 'rgb(32, 144, 32)',
                tension: 0.1
            },
            {
                label: y_datasets[2]['label'],
                data: y_datasets[2]['data'],
                fill: false,
                borderColor: 'rgb(48, 48, 192)',
                tension: 0.1
            }
        ]
    }

    // chart options
    const options = {
        responsive: true,
        plugins: {
            title: {
                display: true,
                text: title,
            }
        },
    };

    return (
        <div className="chart-container">
            <Line data={data} options={options} />
        </div>
    );
}

export const ChartComponent = () => {
    // Sample data for the line chart
    const data = {
        labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August'],  // x_axi
        datasets: [
            {
                label: 'Monthly Sales',
                data: [65, 59, 80, 81, 78, 76, 85, 80],  // y_axi
                fill: false,
                borderColor: 'rgb(48, 48, 192)',
                tension: 0.2,
            },
            {
                label: 'Monthly Sales',
                data: [65, 59, 57, 41, 48, 56, 55, 40],  // y_axi
                fill: false,
                borderColor: 'rgb(192, 48, 48)',
                tension: 0.2,
            },
        ],
    };

    // Chart options
    const options = {
        responsive: true,
        plugins: {
            title: {
                display: true,
                text: 'Sales Over Time',
            },
        },
    };

    return (
        <div className="chart-container">
            <Line data={data} options={options} />
        </div>
    );
};

export default ChartComponent;