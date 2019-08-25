"""
Test file for database methods written in db.py

All test methods must receive client as an argument,
otherwise the database variable won't be configured correctly
"""
from mflix.db import add_comment, update_comment, delete_comment, get_movie
from mflix.api.user import User
from datetime import datetime
import pytest

test_user = {
    "name": "foobar",
    "email": "foobar@baz.com",
}

fake_user = {
    "name": "barfoo",
    "email": "baz@foobar.com"
}
# The Martian
movie_id = "573a13b0f29313caabd3408e"
now = datetime.now()
comment = {
    'text': 'fe-fi-fo-fum',
    'id': ''
}
comment_update = {
    'text': 'frobscottle',
    'id': ''
}
user = User(test_user)
n_user = User(fake_user)


# this test is only here to insert a comment
# you should have implemented this in the previous ticket
@pytest.mark.delete_comments
def test_add_comment_should_be_implemented(client):
    # we need to add a comment
    # this test serves to do that
    result = add_comment(movie_id, user, comment['text'], now)
    comments = get_movie(movie_id).get('comments')
    assert comments[0].get('_id') == result.inserted_id
    assert comments[0].get('text') == comment['text']
    comment['id'] = result.inserted_id


@pytest.mark.delete_comments
def test_should_not_delete_comment_if_email_does_not_match(client):
    result = delete_comment(comment['id'], "fakeemail@email.com")
    assert result.raw_result.get('n') == 0


@pytest.mark.delete_comments
def test_delete_comment_should_delete_if_email_is_owner(client):
    result = delete_comment(comment['id'], test_user['email'])
    assert result.raw_result.get('n') == 1
