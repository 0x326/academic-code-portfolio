// @flow

import 'babel-polyfill'
import $ from 'jquery'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap'

const apiBaseUri = './restFinal.php'

function sendAjaxRequest(ajaxOptions) {
  // Convert jQuery thenables into native Promises
  return Promise.resolve($.ajax(ajaxOptions))
    .catch(({ responseJSON: { msg } }) => {
      throw new Error(msg)
    })
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
    url: `${apiBaseUri}/v1/items/${token}`,
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

async function getItemSummary(token) {
  const ajaxOptions = {
    method: 'GET',
    url: `${apiBaseUri}/v1/itemsSummary/${token}`,
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

  const response = await sendAjaxRequest(ajaxOptions)

  const {
    status,
    msg,
  } = response

  if (status !== 'OK') {
    throw new Error(msg)
  }
}

async function createButtons(token) {
  const errorMessageElem = $('#error-message')

  const items = await getItems()
  $('#buttons')
    .empty()
    .append(
      items.map(({ pk, item }) => $('<button type="button" class="btn btn-primary" />')
        .text(item)
        .click(() => updateItem(pk, token)
          .then(Promise.all([
            updateSummary(token),
            updateLog(token),
          ]))
          .then(() => errorMessageElem.hide())
          .catch(error => errorMessageElem
            .text(`Error getting data. Reason: ${error.message}`)
            .show())))
    )
}

async function updateSummary(token) {
  const items = await getItemSummary(token)
  $('#diary-summary')
    .empty()
    .append(
      items
        .map(({ item, count }) => $('<tr />').append(
          $('<td />').text(item),
          $('<td />').text(count),
        ))
    )
}

async function updateLog(token) {
  const items = await getConsumedItems(token)
  $('#diary-log')
    .empty()
    .append(
      items
        .slice(0, 20)
        .map(({ item, timestamp }) => $('<tr />').append(
          $('<td />').text(item),
          $('<td />').text(timestamp),
        ))
    )
}

// eslint-disable-next-line no-undef
$(document).ready(() => $('#login-form')
  .submit(async (evt) => {
    evt.preventDefault()

    const formData = {}
    for (const { name, value } of $('#login-form').serializeArray()) {
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
      $('#login').hide()
      $('#content').show()

      return Promise.all([
        createButtons(token),
        updateSummary(token),
        updateLog(token),
      ])
        .then(() => errorMessageElem.hide())
        .catch(error => errorMessageElem
          .text(`Error getting data. Reason: ${error.message}`)
          .show())
    } catch (error) {
      errorMessageElem
        .text('Invalid login')
        .show()
      return Promise.resolve()
    }
  }))