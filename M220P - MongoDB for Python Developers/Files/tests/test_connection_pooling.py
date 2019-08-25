"""
Test file for database methods written in db.py

All test methods must receive client as an argument,
otherwise the database variable won't be configured correctly
"""
from mflix.db import get_configuration
import pytest


@pytest.mark.connection_pooling
def test_max_pool_size(client):
    (pool_size, _, _) = get_configuration()
    assert pool_size == 50
