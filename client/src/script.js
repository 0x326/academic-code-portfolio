// @flow

import 'babel-polyfill'
import $ from 'jquery'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap'

const apiBaseUri = 'http://localhost:8080/restFinal.php'

async function sendAjaxRequest(ajaxOptions) {
  // Convert jQuery thenables into native Promises
  const data = await Promise.resolve($.ajax(ajaxOptions))
  // if (data.status !== 'OK') {
  //   throw new Error('Server status !== OK')
  // }
  return data
}

async function submitCredentials(username, password) {
  const ajaxOptions = {
    method: 'POST',
    url: `${apiBaseUri}/v1/user`,
    data: JSON.stringify({
      user: username,
      password,
    }),
    contentType: 'application/json',
    dataType: 'json',
  }

  const response = await sendAjaxRequest(ajaxOptions)

  const {
    status,
    msg,
    token,
  } = response

  if (status !== 'OK') {
    throw new Error(msg)
  }

  return token
}

async function getItems() {
  const ajaxOptions = {
    method: 'GET',
    url: `${apiBaseUri}/v1/items`,
    contentType: 'application/json',
    dataType: 'json',
  }
  const response = await sendAjaxRequest(ajaxOptions)

  const {
    status,
    msg,
    items,
  } = response

  if (status !== 'OK') {
    throw new Error(msg)
  }

  return items
}

async function getConsumedItems(token) {
  const ajaxOptions = {
    method: 'GET',
    url: `${apiBaseUri}/items/${token}`,
    contentType: 'application/json',
    dataType: 'json',
  }
  const response = sendAjaxRequest(ajaxOptions)

  const {
    status,
    msg,
    items,
  } = response

  if (status !== 'OK') {
    throw new Error(msg)
  }

  return items
}

async function getItemSummary(token) {
  const ajaxOptions = {
    method: 'GET',
    url: `${apiBaseUri}/v1/itemsSummary/${token}`,
    contentType: 'application/json',
    dataType: 'json',
  }

  const response = sendAjaxRequest(ajaxOptions)

  const {
    status,
    msg,
    items,
  } = response

  if (status !== 'OK') {
    throw new Error(msg)
  }

  return items
}

async function updateItem(itemKey, token) {
  const ajaxOptions = {
    method: 'POST',
    url: `${apiBaseUri}/v1/items`,
    data: JSON.stringify({
      token,
      itemFK: itemKey,
    }),
    contentType: 'application/json',
    dataType: 'json',
  }

  const response = sendAjaxRequest(ajaxOptions)

  const {
    status,
    msg,
  } = response

  if (status !== 'OK') {
    throw new Error(msg)
  }
}

$(document).ready(() => {
  // TODO: Add event handlers
  $('#login-form').submit(async (evt) => {
    evt.preventDefault()

    const formData = {}
    for (const {name, value} of $('#login-form').serializeArray()) {
      formData[name] = value
    }
    const {
      username,
      password,
    } = formData

    const errorMessageElem = $('#error-message')

    try {
      const token = await submitCredentials(username, password)
      errorMessageElem.hide()

      return Promise.all([
        Promise.resolve().then(() => getItems())
          .then(items =>
            $('#FoodButton').append(
              items.map(({ pk, item }) =>
                $('<button/>')
                  .text(item)
                  .click(() => updateItem(pk, token))))), // TODO: Update table

        Promise.resolve().then(() => getConsumedItems())
          .then((items) =>
            $('#DiaryEntry').append(
              $('<table class="table" />').append(
                '<thead>' +
                '<td>Item</td>' +
                '<td>Timestamp</td>' +
                '</thead>',
                $('<tbody />').append(
                  items
                    .slice(-20)
                    .map(({ item, timestamp }) =>
                      $('<tr />').append(
                        $('<td />').text(item),
                        $('<td />').text(timestamp),
                      )))))),

        Promise.resolve().then(() => getItemSummary())
          .then(items =>
            $('#DiarySummary').append(
              $('<table class="table" />').append(
                '<thead>' +
                '<td>Item</td>' +
                '<td>Count</td>' +
                '</thead>',
                $('<tbody />').append(
                  items
                    .map(({ item, count }) =>
                      $('<tr />').append(
                        $('<td />').text(item),
                        $('<td />').text(count),
                      )))))),
      ])
        .catch(() => errorMessageElem
          .text('Error getting data')
          .show())
    } catch (error) {
      errorMessageElem
        .text('Invalid login')
        .show()
    }
  })
})
