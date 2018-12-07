// @flow

import 'babel-polyfill'
import $ from 'jquery'
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap'

const apiBaseUri = 'http://localhost:8080/rest.php'

async function sendAjaxRequest (ajaxOptions) {
  // Convert jQuery thenables into native Promises
  const data = await Promise.resolve($.ajax(ajaxOptions))
  // if (data.status !== 'OK') {
  //   throw new Error('Server status !== OK')
  // }
  return data
}

async function submitCredentials (username, password) {
  const ajaxOptions = {
    method: 'POST',
    url: `${apiBaseUri}/v1/user`,
    data: JSON.stringify({
      user: username,
      password
    }),
    contentType: 'application/json',
    dataType: 'json'
  }

  const userToken = await sendAjaxRequest(ajaxOptions)

  const {
    status,
    msg,
    token
  } = userToken

  if (status !== 'OK') {
    throw new Error(msg)
  }

  return token
}

async function getItems () {
  const itemDemand = {
    method: 'GET',
    url: `${apiBaseUri}/v1/items`,
    contentType: 'application/json',
    dataType: 'json'
  }
  const holder = sendAjaxRequest(itemDemand)

  const {
    status,
    msg,
    pk,
    item,
    items = [pk, item]
  } = holder

  if (status !== 'OK') {
    throw new Error(msg)
  }
  if (msg !== 'OK') {

  }
  if(items !== 'OK'){

  }

  return holder
}

async function getConsumedItems () {
  const itemsConsumed = {
    method: 'GET',
    url: `${apiBaseUri}/items/token`,
    contentType: 'application/json',
    dataType: 'json'
  }
  const consumed = sendAjaxRequest(itemsConsumed)

  const {
    status,
    pk,
    msg,
    timestamp,
    items = [pk, item, timestamp]
  } = consumed
  if (consumed !== 'OK') {
    throw new Error(msg)
  }
  if (status !== 'OK') {
    throw new Error(msg)
  }
  if (pk !== 'OK') {

  }

  if (msg !== 'OK') {
  }
  if (status !== 'OK') {
  }
  if (items !== 'OK') {
  }
  if (timestamp !== 'OK') {

  }
  return consumed
}

async function getItemSummary () {
  const itemsSummarized = {
    method: 'GET',
    url: `${apiBaseUri}/v1/itemsSummary/token`,
    contentType: 'application/json',
    dataType: 'json'
  }

  const summarized = sendAjaxRequest(itemsSummarized)

  const {
    status = '',
    msg = '',
    item = '',
    count,
    items =[item, count]

  } = summarized
  if (status !== 'OK') {

  }
  if (msg !== 'OK') {

  }
  if(items !== 'OK'){

  }
  return summarized
}

async function updateItem () {
  const token = ''
  const itemFK = ''
  const itemsUpdated = {
    method: 'POST',
    url: `${apiBaseUri}/v1/items`,
    data: JSON.stringify({
      token,
      itemFK
    }),
    contentType: 'application/json',
    dataType: 'json'
  }

  const updated = sendAjaxRequest(itemsUpdated)
  const {
    status,
    msg
  } = updated
  if (status !== 'OK') {
    throw new Error(msg)
  }
}

$(document).ready(() => {
  // TODO: Add event handlers
  $('#login-form').submit((evt) => {
    evt.preventDefault()
    const formData = {}
    for (const { name, value } of $('#login-form').serializeArray()) {
      formData[name] = value
    }
    const {
      username,
      password
    } = formData

    const errorMessageElem = $('#error-message')

    const login = submitCredentials(username, password)
      .catch(() => errorMessageElem.text('Invalid login').show())
      .then(() => errorMessageElem.hide())
    Promise.all([
      login.then(() => getItems())
      // append to the following line.
        .then(items => items.forEach(({ pk, item }) => $('<button></button>').text(item).click(() => updateItem(pk))).append(item[0])),
      login.then(() => getConsumedItems())
        .then((items => items.forEach(({item, count}) => items[count] > 0))),
      login.then(() => getItemSummary())
        .then((items => items.forEach(({pk, item})=> $(items[item])))),
      login.then(() => updateItem())
        .then(items => (items.forEach(({item, count}) =>
          $('<button></button>').text(item).click(() =>(item[count] = item[count]+1)))))
    ])
      .catch(() => errorMessageElem.text('Error getting data').show())
  })
})
