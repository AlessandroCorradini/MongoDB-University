import pytest
from mflix.factory import create_app

import os
import configparser

config = configparser.ConfigParser()
config.read(os.path.abspath(os.path.join(".ini")))


@pytest.fixture
def app():
    app = create_app()
    app.config['SECRET_KEY'] = config['TEST']['SECRET_KEY']
    app.config['MFLIX_NS'] = config['PROD']['MFLIX_NS']
    app.config['MFLIX_DB_URI'] = config['TEST']['MFLIX_DB_URI']
    return app
