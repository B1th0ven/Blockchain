$(document).on('click','#menu-toggle',function(e){
    e.preventDefault();
    $("#wrapper").toggleClass("toggled");
})

var sideBarHandler=function(){
    if($(window).width()>="768")
        $("#wrapper").addClass("toggled");
    else
        $("#wrapper").removeClass("toggled");

    setTimeout(() => {
        $("#wrapper,#sidebar-wrapper").addClass("animated");
    }, 500);
}

$(sideBarHandler)
$(window).resize(sideBarHandler)

$(document).on('click','.btn-select',function(e){
    e.preventDefault();
    $(this).toggleClass("toggled");
})

/*
$(document).on('click','.card-header',function(e){
    e.preventDefault();
    var icon = $(this).find("i.fa-chevron-up, i.fa-chevron-down")
    if (!icon) return
    var collapse = $(this).next()
    if (!collapse) return

    if ( collapse.hasClass("collapsing"))
    {
        if (icon.hasClass("fa-chevron-up"))
        {
            icon.removeClass("fa-chevron-up")
            icon.addClass("fa-chevron-down")
        }else
        {
            icon.removeClass("fa-chevron-down")
            icon.addClass("fa-chevron-up")
        }
    }

})
*/
