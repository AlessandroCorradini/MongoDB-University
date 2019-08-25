"""
Test file for database methods written in db.py

All test methods must receive client as an argument,
otherwise the database variable won't be configured correctly
"""
from mflix.db import get_movie
import pytest


@pytest.mark.error_handling
def test_no_error(client):
    response = get_movie("foobar")
    assert response is None
