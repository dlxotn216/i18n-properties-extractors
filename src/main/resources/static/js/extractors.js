/**
 * Created by taesu on 2018-08-16.
 */

const extractorsModule = (function () {
    const firstCellTemplate = `<td>#{INDEX}#</td>`;
    const secondCellTemplate = `<td><input type="file" name="files[#{INDEX}#]"/></td>`;
    const thirdCellTemplate = `
    <select name="locales[#{INDEX}#]"></select>
    `;

    const getCellTemplate = function (celltemplate, rowIndex) {
        return celltemplate.replace(/#{INDEX}#/gi, rowIndex);
    };

    const table = document.getElementById('extractors-table');
    const tableBody = document.getElementById('extractors-table-tbody');
    const addRowButtons = [...document.getElementsByClassName('add-row')];
    const deleteRowButtons = [...document.getElementsByClassName('delete-row')];

    const addRowEvent = function (e) {
        const index = tableBody.rows.length;
        let row = tableBody.insertRow(index);

        row.insertCell(0).innerHTML = getCellTemplate(firstCellTemplate, index);
        row.insertCell(1).innerHTML = getCellTemplate(secondCellTemplate, index);
        row.insertCell(2).innerHTML = getCellTemplate(thirdCellTemplate, index);

        const selectElement = document.getElementsByName('locales[' + index + ']')[0];
        [...document.getElementsByTagName('option')].forEach(option => {
            let optionElement = option.cloneNode(true);
            selectElement.appendChild(optionElement);
        });


    };
    const deleteRowEvent = function (e) {
        const index = tableBody.rows.length - 1;
        if (index < 1) {
            return;
        }
        tableBody.deleteRow(index);
    };

    const bindFromEvents = function () {
        if (addRowButtons && addRowButtons.length > 0) {
            addRowButtons.forEach(btn => {
                btn.addEventListener('click', addRowEvent);
            });
        }

        if (deleteRowButtons && deleteRowButtons.length > 0) {
            deleteRowButtons.forEach(btn => {
                btn.addEventListener('click', deleteRowEvent);
            });
        }
    };


    return {
        init: function () {
            bindFromEvents();
        }
    }
})();


extractorsModule.init();