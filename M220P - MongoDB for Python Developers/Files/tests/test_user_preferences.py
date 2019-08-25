import pytest
from mflix.db import update_prefs, delete_user, get_user, add_user
from pymongo.results import UpdateResult


@pytest.mark.user_preferences
def test_invalid_user_should_not_have_preferences(client):
    # delete the test user if it exists
    delete_user('foobaz@bar.com')
    preferences = {
        "color": "green",
        "favorite_letter": "q",
        "favorite_number": 42
    }

    result = update_prefs('foobaz@bar.com', preferences)

    assert result.get('error', None) is not None
    assert isinstance(result, UpdateResult) is False


@pytest.mark.user_preferences
def test_should_not_allow_None_as_prefs(client):
    add_user("foo", "foobaz@bar.com", "foobar")
    prefs = None
    update_prefs("foobaz@bar.com", prefs)

    user = get_user("foobaz@bar.com")
    assert user.get("preferences") == {}


@pytest.mark.user_preferences
def test_valid_user_preferences(client):
    # create the user
    add_user("foo", "foobaz@bar.com", "foobar")
    preferences = {
        "favorite_cast_member": "Goldie Hawn",
        "favorite_genre": "Comedy",
        "preferred_ratings": ["G", "PG", "PG-13"]
    }

    # update user preferences
    result = update_prefs("foobaz@bar.com", preferences)
    assert result.matched_count == 1
    assert result.modified_count == 1

    # get the user
    user = get_user("foobaz@bar.com")
    assert user.get('preferences') is not None
    new_preferences = {
        "favorite_cast_member": "Daniel Day-Lewis",
        "favorite_genre": "Drama",
        "preferred_ratings": ["R"]
    }
    result = update_prefs(user.get('email'), new_preferences)
    assert result.matched_count == 1
    assert result.modified_count == 1
    user = get_user("foobaz@bar.com")
    assert user.get("preferences") == new_preferences


@pytest.mark.user_preferences
def test_empty_prefs_are_valid(client):
    new_prefs = {}
    result = update_prefs("foobaz@bar.com", new_prefs)

    assert result.matched_count == 1
    assert result.modified_count == 1

    user = get_user("foobaz@bar.com")
    assert user.get("preferences") == {}
