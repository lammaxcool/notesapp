import json
import socket
from time import sleep

import requests
from flask import Flask

app = Flask(__name__)

BASE_CONSUL_URL = 'http://consul:8500'

SERVICE_ADDRESS = socket.gethostbyname(socket.gethostname())
PORT = 5000
EXPOSED_PORT = 5000


@app.route('/hello')
def home():
    return 'Hello from python server through STATISTIC service! // {address}'.format(address=SERVICE_ADDRESS)


@app.route('/health')
def hello_world():
    data = {
        'status': 'healthy'
    }
    return json.dumps(data)


@app.route('/register')
def register():
    url = BASE_CONSUL_URL + '/v1/agent/service/register'
    data = {
        'Name': 'python-server',
        'Tags': ['flask'],
        'Address': SERVICE_ADDRESS,
        'Port': EXPOSED_PORT,
        'Check': {
            'http': 'http://{address}:{port}/health'.format(address=SERVICE_ADDRESS, port=PORT),
            'interval': '10s'
        }
    }
    app.logger.info('Service registration parameters: ', data)
    res = requests.put(
        url,
        data=json.dumps(data)
    )
    return res.text


if __name__ == '__main__':
    sleep(8)
    try:
        app.logger.info(register())
    except:
        app.logger.error('Failed to register service')
        pass

    app.run(host="0.0.0.0", port=PORT)
