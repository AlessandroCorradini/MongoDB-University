"""
Test file for facet search method written in db.py

All test methods must receive client as an argument,
otherwise the database variable won't be configured correctly
"""
import pytest
from mflix.db import get_movies_faceted


@pytest.mark.facets
def test_faceted_search_should_return_rating_and_runtime_buckets(client):
    filter = {'cast': ['Tom Hanks']}
    result = get_movies_faceted(filter, 0, 20)
    # expecting the first entry in the returned tuple to be a dictionary with
    # the key 'movies'
    assert len(result[0]['movies']) == 20
    assert len(result[0]['rating']) == 5
    assert len(result[0]['runtime']) == 4
    # expecting the second entry in the returned tuple to be the number of results
    assert result[1] == 37



@pytest.mark.facets
def test_faceted_search_should_also_support_paging(client):
    filter = {'cast': ['Susan Sarandon'], }
    result = get_movies_faceted(filter, 1, 20)
    assert(len(result[0]['movies'])) == 18
    assert len(result[0]['rating']) == 3
    assert len(result[0]['runtime']) == 4
    assert result[1] == 38
