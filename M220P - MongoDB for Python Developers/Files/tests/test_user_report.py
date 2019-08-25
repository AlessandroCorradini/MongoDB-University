"""
Test file for database methods written in db.py

All test methods must receive client as an argument,
otherwise the database variable won't be configured correctly
"""
from mflix.db import most_active_commenters
import pytest


@pytest.mark.user_report
def test_cast_popularity(client):
    result = most_active_commenters()
    assert len(result) == 20


@pytest.mark.user_report
def test_check_report(client):
    result = most_active_commenters()
    assert {'_id': 'roger_ashton-griffiths@gameofthron.es', 'count': 331} in result
