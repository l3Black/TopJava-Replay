var ajaxMealsUrl = "ajax/profile/meals/";

function updateFilteredTable() {
    $.ajax({
        type: "GET",
        url: ajaxMealsUrl + "filter",
        data: $("#filter").serialize()
    }).done(updateTableByData);
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get(ajaxMealsUrl, updateTableByData);
}

$(function () {
    makeEditable({
        ajaxUrl: ajaxMealsUrl,
        datatableApi: $("#datatable").DataTable({
            "ajax": {
                "url": ajaxMealsUrl,
                "dataSrc": ""
            },
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime",
                    "render": function (date, type, row) {
                        if (type === "display") {
                            return moment(date).format("YYYY-MM-DD HH:mm");
                        }
                        return date;
                    }
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderEditBtn
                },
                {
                    "orderable": false,
                    "defaultContent": "Delete",
                    "render": renderDeleteBtn
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ],
            "createdRow": function (row, data, dataIndex) {
                $(row).attr("data-mealExcess", data.excess)
            }
        }),
        updateTable: updateFilteredTable
    });
});

var lan = window.navigator ? (window.navigator.language ||
    window.navigator.systemLanguage ||
    window.navigator.userLanguage) : "en";
lan = lan.substr(0, 2).toLowerCase();

jQuery.datetimepicker.setLocale(lan);

$('#startDate, #endDate').datetimepicker({
    timepicker: false,
    format: 'Y-m-d'
});

$('#startTime, #endTime').datetimepicker({
    datepicker: false,
    format: 'H:i'
});

$('#dateTime').datetimepicker({
    format: 'Y-m-d H:i'
})