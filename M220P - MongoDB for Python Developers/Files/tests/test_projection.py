"""
Test file for database methods written in db.py

All test methods must receive client as an argument,
otherwise the database variable won't be configured correctly
"""
import pytest
from mflix.db import get_movies_by_country


@pytest.mark.projection
def test_basic_country_search_db(client):
    countries = ['Kosovo']
    result = get_movies_by_country(countries)
    assert len(result) == 2


@pytest.mark.projection
def test_basic_country_search_shape_db(client):
    countries = ['Russia', 'Japan']
    result = get_movies_by_country(countries)
    assert len(result) == 1237
    # we should only be returning the _id and title fields
    encountered_keys = {}
    for movie in result:
        for k in movie:
            encountered_keys[k] = encountered_keys.get(k, 0) + 1

    assert len(list(encountered_keys.keys())) == 2
    assert encountered_keys['_id'] == 1237
    assert encountered_keys['title'] == 1237
