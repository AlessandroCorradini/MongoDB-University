from mflix.factory import create_app

import os
import configparser


config = configparser.ConfigParser()
config.read(os.path.abspath(os.path.join(".ini")))

if __name__ == "__main__":
    app = create_app()
    app.config['DEBUG'] = True
    app.config['MFLIX_DB_URI'] = config['PROD']['MFLIX_DB_URI']
    app.config['MFLIX_NS'] = config['PROD']['MFLIX_NS']
    app.config['SECRET_KEY'] = config['PROD']['SECRET_KEY']

    app.run()
