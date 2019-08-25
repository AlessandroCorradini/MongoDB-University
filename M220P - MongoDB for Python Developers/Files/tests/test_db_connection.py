"""
Test file for database methods written in db.py

All test methods must receive client as an argument,
otherwise the database variable won't be configured correctly
"""
from mflix.db import get_movies, get_movie
import pytest
from pymongo import MongoClient


def get_coll_names(config):
    """
    Method used in unit tests. Do not alter. You can cheat, but you only defeat
    yourself.
    """
    db = MongoClient(config['MFLIX_DB_URI'])["sample_mflix"]
    return db.list_collection_names()


@pytest.mark.connection
@pytest.mark.usefixtures('config')
def test_atlas_setup(client, config):
    result = get_coll_names(config)
    assert all(x in result for x in ['users', 'comments', 'movies'])
    assert len(result) >= 5


@pytest.mark.connection
def test_basic_movies(client):
    (actual, num_found) = get_movies({}, 0, 20)
    assert num_found == 23539
    assert(len(list(actual))) == 20


@pytest.mark.connection
def test_search_by_movie_id(client):
    actual = get_movie("573a13acf29313caabd29647")
    assert actual['title'] == 'King Kong'


@pytest.mark.connection
def test_simple_text_search(client):
    (actual, _) = get_movies({"$text": {"$search": "King Kong"}}, 0, 20)
    assert len(list(actual)) >= 4
