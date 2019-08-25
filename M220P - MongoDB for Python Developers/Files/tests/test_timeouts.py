"""
Test file for database methods written in db.py

All test methods must receive client as an argument,
otherwise the database variable won't be configured correctly
"""
from mflix.db import get_configuration
import pytest


@pytest.mark.timeouts
def test_proper_type(client):
    (_, w_concern, _) = get_configuration()
    assert w_concern._WriteConcern__document['wtimeout'] == 2500
