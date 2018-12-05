// @flow

import 'babel-polyfill'
import $ from 'jquery'
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap'

async function sendAjaxRequest (ajaxOptions) {
  // Convert jQuery thenables into native Promises
  const data = await Promise.resolve($.ajax(ajaxOptions))
  // if (data.status !== 'OK') {
  //   throw new Error('Server status !== OK')
  // }
  return data
}

async function submitCredentials (username, password, retryLimit = 3) {
  if (retryLimit < 0) {
    throw new Error('retryLimit must be non-negative')
  }

  const ajaxOptions = {
    method: 'POST',
    url: 'https://ceclnx01.cec.miamioh.edu/~campbest/cse383/forms1/form-ajax.php',
    data: JSON.stringify({
      username,
      password
    }),
    contentType: 'application/json',
    dataType: 'json'
  }

  let ajaxSubmission = sendAjaxRequest(ajaxOptions)

  for (let i = 0; i < retryLimit; i += 1) {
    ajaxSubmission = ajaxSubmission.catch(() => sendAjaxRequest(ajaxOptions))
  }
  return ajaxSubmission
  // .catch(() => $('#error').text('Form cannot be submitted since server is unavailable'))
  // .then(({ data }) => {
  //   $('#formDiv').hide()
  //   $('#submissions-table-div').show()
  //   populateTableData('#submissions-table', data)
  //   $('#error').text('')
  // })
}

async function requestData () {
  return sendAjaxRequest()
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
    submitCredentials(username, password)
      .catch(() => console.log('TODO: Tell the user his password is wrong'))
      .then(() => requestData())
      .then((/* data */) => {
        $('#content')
        // TODO: Do something
      })
  })
})
