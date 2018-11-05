function myCheckSubmit(evt) {
  const form = document.getElementById('form')
  const formFields = form.querySelectorAll('input[type=text], textarea')

  for (const elem of formFields) {
    if (elem.value.length < 3) {
      const error = document.getElementById('error')
      error.innerText = 'You must enter at least three characters into all text fields'
      return false
    }
  }

  error.innerText = ''
  return true
}

async function sendAjaxRequest(ajaxOptions) {
  // Convert jQuery thenables into native Promises
  const data = await Promise.resolve($.ajax(ajaxOptions))
  if (data.status !== 'OK') {
    throw new Error('Server status !== OK')
  }
  return data
}

async function submitFormData(data, retryLimit = 3) {
  if (retryLimit < 0) {
    throw new Error('retryLimit must be non-negative')
  }

  const ajaxOptions = {
    method: 'POST',
    url: 'https://ceclnx01.cec.miamioh.edu/~campbest/cse383/forms1/form-ajax.php',
    data: JSON.stringify(data),
    contentType: 'application/json',
    dataType: 'json',
  }

  let ajaxSubmission = sendAjaxRequest(ajaxOptions)

  for (let i = 0; i < retryLimit; i += 1) {
    ajaxSubmission = ajaxSubmission.catch(() => sendAjaxRequest(ajaxOptions))
  }
  return ajaxSubmission
    .catch(() => $('#error').text('Form cannot be submitted since server is unavailable'))
    .then(({ data }) => {
      $('#formDiv').hide()
      $('#submissions-table-div').show()
      populateTableData('#submissions-table', data)
      $('#error').text('')
    })
}

function populateTableData(tableSelector, entries) {
  // Assume there is at least one entry
  const tableHeaderKeys = Object.keys(entries[0])

  // Add table headers
  $(tableSelector).html(`
    <thead>
      <tr>
        ${tableHeaderKeys
          .map(property => `<th scope="col">${property}</th>`)
          .join('\n')}
      </tr>
    </thead>
    <tbody></tbody>
  `)

  // Add table rows
  const tbody = $('tbody', tableSelector)
  for (const entry of entries) {
    const rowValues = tableHeaderKeys
      .map(key => entry[key])
      .map(val => `<td>${val}</td>`)
      .join('\n')
    tbody.append(`<tr>${rowValues}</tr>`)
  }
}

$(document).ready(() => {
  $('#start-new-form-button').click(() => {
    $('#formDiv').show()
    $('#submissions-table-div').hide()
    $('input[type=text], textarea', '#form').val('')
  })
  $('#form').submit((evt) => {
    evt.preventDefault()
    const inputIsValid = myCheckSubmit()
    if (inputIsValid === true) {
      const dataToSend = {}
      for (const { name, value } of $('#form').serializeArray()) {
        dataToSend[name] = value
      }
      submitFormData(dataToSend)
    }
  })
})
