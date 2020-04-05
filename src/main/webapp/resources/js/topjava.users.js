var context = {
    ajaxUrl: "ajax/admin/users/",
    datatableApi: $("#datatable").DataTable({
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "name"
            },
            {
                "data": "email"
            },
            {
                "data": "roles"
            },
            {
                "data": "enabled"
            },
            {
                "data": "registered"
            },
            {
                "defaultContent": "Edit",
                "orderable": false
            },
            {
                "defaultContent": "Delete",
                "orderable": false
            }
        ],
        "order": [
            [
                0,
                "asc"
            ]
        ]
    })
};

$(function () {
    makeEditable(context);
    $(".check").change(function () {
        var isEnabled = this.checked;
        $.ajax({
            type: "PUT",
            url: context.ajaxUrl + $(this).parents("tr").attr("id") + "?isEnable=" + isEnabled
        }).done(function () {
            updateTable();
            successNoty("isEnable: " + isEnabled);
        })
    })
});