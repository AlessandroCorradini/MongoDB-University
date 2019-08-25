"""
Test file for database methods written in db.py

All test methods must receive client as an argument,
otherwise the database variable won't be configured correctly
"""
from mflix.db import get_movie
from random import randint
import pytest


@pytest.mark.get_comments
def test_fetch_comments_for_movie(client):
    # Lady and the Tramp
    movie_id = "573a1394f29313caabcdfd61"
    result = get_movie(movie_id)
    assert len(result.get('comments', [])) == 126


@pytest.mark.get_comments
def test_fetch_comments_for_another_movie(client):
    # 300
    movie_id = "573a13b1f29313caabd36321"
    result = get_movie(movie_id)
    assert len(result.get('comments', [])) == 145


@pytest.mark.get_comments
def test_comments_should_be_sorted_by_date(client):
    # in order from most to least recent
    movie_ids = [
        "573a1391f29313caabcd8414",
        "573a1392f29313caabcd9d4f",
        "573a1392f29313caabcdae3d",
        "573a1392f29313caabcdb40b",
        "573a1392f29313caabcdb585",
        "573a1393f29313caabcdbe7c",
        "573a1393f29313caabcdd6aa"
    ]
    for id in movie_ids:
        comments = get_movie(id).get('comments', [])
        test_comments = sorted(
            comments[:],
            key=lambda c: c.get('date'),
            reverse=True
        )
        for _ in range(0, min(10, len(comments))):
            rand_int = randint(0, len(comments) - 1)
            assert comments[rand_int] == test_comments[rand_int]
