"""
Test file for database methods written in db.py

All test methods must receive client as an argument,
otherwise the database variable won't be configured correctly
"""
import pytest
from mflix.db import get_movies


@pytest.mark.paging
def test_supports_paging_by_cast(client):
    filter = {'cast': ['Tom Hanks']}
    (movies0, results0) = get_movies(filter, 0, 20)
    assert len(list(movies0)) == 20
    assert results0 == 37
    assert movies0[0].get('title') == 'Forrest Gump'
    (movies1, results1) = get_movies(filter, 1, 20)
    assert len(list(movies1)) == 17
    assert movies1[0].get('title') == 'Captain Phillips'


@pytest.mark.paging
def test_supports_paging_by_genre(client):
    filter = {'genres': ['History']}
    (movies0, results0) = get_movies(filter, 0, 20)
    assert len(list(movies0)) == 20
    assert results0 == 999
    assert movies0[0].get('title') == 'Braveheart'
    last_page = int(999 / 20)
    (movies2, results2) = get_movies(filter, last_page, 20)
    assert len(list(movies2)) == 19
    assert movies2[0].get('title') == 'Only the Dead'


@pytest.mark.paging
def test_supports_paging_by_text(client):
    filter = {'text': 'bank robbery'}
    (movies0, results0) = get_movies(filter, 0, 20)
    assert len(list(movies0)) == 20
    assert results0 == 475
    assert movies0[0].get('title') == 'The Bank'
    last_page = int(475 / 20)
    (movies2, results2) = get_movies(filter, last_page, 20)
    assert len(list(movies2)) == 15
    assert movies2[0].get('title') == "Ugetsu"
