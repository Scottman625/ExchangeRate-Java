

function queryRate() {
    console.log("queryRate");
    var currencyPair = document.getElementById('currencyPairSelect').value;
    if (currencyPair == "ALL") {
        window.location.reload();
    } else {
        var currencies = currencyPair.split('/'); // 分割字符串为两部分
        var fromCurrency = currencies[0];
        var toCurrency = currencies[1];

        fetch(`/api/rates/${fromCurrency}/to/${toCurrency}`) // 使用模板字符串构建URL
            .then(function(response) {
                if (!response.ok) {
                    throw new Error('Network response was not ok ' + response.statusText);
                }
                return response.json();
            })
            .then(function(data) {
                var tableBody = document.getElementById('ratesTableBody'); // 确保你有一个带有ID的<tbody>元素
                tableBody.innerHTML = ''; // 清空现有的表格内容

                // 假设返回的data是一个对象数组
                data.forEach(function(rate) {
                    var formattedDate = new Date(rate.lastUpdated).toLocaleDateString('en-US', { year: 'numeric', month: '2-digit', day: '2-digit' });
                    var newRow = `<tr>
                        <td>${rate.currencyPair}</td>
                        <td>${rate.rate}</td>
                        <td>${formattedDate}</td>
                        <td>
                            <button type="button" onclick="prepareUpdate('${rate.id}')">Update</button>
                        </td>
                        <td>
                            <button type="button" onclick="deleteRate('${rate.id}')">Delete</button>
                        </td>
                    </tr>`;
                    tableBody.innerHTML += newRow; // 添加新行到表格中
                });
            })
            .catch(function(error) {
                console.error('Error fetching rate:', error);
            });
    }
}


function addRateToList() {
    console.log("addRate");
    var select = document.getElementById('currencyPairSelect');
    var currencyPair = document.getElementById('currencyPairSelect').value;
    var currencies = currencyPair.split('/'); // 分割字符串为两部分
    var fromCurrency = currencies[0];
    var toCurrency = currencies[1];

    // Replace with the actual API URL you have
    var apiUrl = `http://localhost:8080/api/findRates/${fromCurrency}/to/${toCurrency}`;

    fetch(apiUrl)
        .then(response => response.json())
        .then(data => {
            // Assume the response data is an object like { currencyPair: "USD/TWD", rate: "28.3" }
            var table = document.getElementById('ratesTable').getElementsByTagName('tbody')[0];
            var newRow = table.insertRow();
            var cell1 = newRow.insertCell(0);
            var cell2 = newRow.insertCell(1);
            var cell3 = newRow.insertCell(2);


            cell1.textContent = data.currencyPair;
            cell2.textContent = data.rate;
            cell3.textContent = data.lastUpdated; // Use the server's response or the current time

            // Clear previous selection
            select.selectedIndex = 0;
            window.location.reload();

        })
        .catch(error => console.error('Error:', error));
}
// function prepareUpdate(id) {
//     // 填充表单以更新
//     var rateIdField = document.getElementById('rateId');
//     var currencyPairField = document.getElementById('currencyPair');
//     var rateField = document.getElementById('rate');
//
//     // 假设每行汇率数据中有一个隐藏的data属性包含了JSON格式的汇率对象
//     var rateData = document.querySelector('tr[data-rate-id="' + id + '"]').dataset.rate;
//     var rate = JSON.parse(rateData);
//
//     rateIdField.value = id;
//     currencyPairField.value = rate.currencyPair;
//     rateField.value = rate.rate;
// }

function preparePost(){

    document.getElementById('createDialog').showModal();
}

function prepareUpdate(id) {
    // var rateData = document.querySelector('tr[data-rate-id="' + id + '"]').dataset.rate;
    // var rate = JSON.parse(rateData);

    // 填充更新表单的字段
    document.getElementById('updateRateId').value = id;
    // document.getElementById('updateCurrencyPair').value = rate.currencyPair;
    // document.getElementById('updateRate').value = rate.rate;

    // 显示更新弹窗
    document.getElementById('updateDialog').showModal();
}

function submitPost() {
    var currencyPair = document.getElementById('createCurrencyPair').value; // 假设你已经有一个元素包含货币对
    // var rateValue = document.getElementById('createRate').value; // 假设你已经有一个元素包含汇率

    var data = {
        currencyPair: currencyPair,
        // rate: rateValue,
        lastUpdated:Date.now()
    };

    fetch('/api/postRate', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => {
            if (response.ok) {
                return response.json(); // or response.text() if the server doesn't return JSON
            }
            throw new Error('Request failed: ' + response.status);
        })
        .then(data => {
            console.log('Create successful:', data);
            closecreateDialog(); // 如果更新成功，关闭弹窗
            window.location.reload();
        })
        .catch(error => {
            console.error('Create failed:', error);
        });
}
function submitUpdate() {
    var id = document.getElementById('updateRateId').value; // 假设你已经有一个元素包含id
    var currencyPair = document.getElementById('updateCurrencyPair').value; // 假设你已经有一个元素包含货币对
    var rateValue = document.getElementById('updateRate').value; // 假设你已经有一个元素包含汇率

    var data = {
        currencyPair: currencyPair,
        rate: rateValue,
        lastUpdated:Date.now()
    };

    fetch('/api/rates/' + id, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => {
            if (response.ok) {
                return response.json(); // or response.text() if the server doesn't return JSON
            }
            throw new Error('Request failed: ' + response.status);
        })
        .then(data => {
            console.log('Update successful:', data);
            closeupdateDialog(); // 如果更新成功，关闭弹窗
            window.location.reload();
        })
        .catch(error => {
            console.error('Update failed:', error);
        });
}
function closeupdateDialog() {
    // 关闭弹窗
    document.getElementById('updateDialog').close();
}

function closecreateDialog() {
    // 关闭弹窗
    document.getElementById('createDialog').close();
}
function deleteRate(id) {
    fetch('/api/rates/' + id, {
        method: 'DELETE'
    }).then(response => {
        if (response.ok) {
            console.log('Deleted rate with id:', id);
            window.location.reload();
        } else {
            throw new Error('Rate could not be deleted.');
        }
    }).catch((error) => {
        console.error('Error:', error);
    });
}
