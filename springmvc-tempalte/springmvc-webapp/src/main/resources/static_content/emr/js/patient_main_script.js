$(function() {
	// $().version();
	// hoo.alert1();
	$(window).scroll(function(e) {
		if ($(window).scrollTop() == 0) {
			$("a.backtop").hide();
		} else {
			if($("a.backtop").size() == 0){
				var backTop = "<a class=\"backtop\" href=\"javascript:void\"></a>";
				$(backTop).appendTo($("body")).click(function() {
					$(document).scrollTop(0);
				});
			}
			$("a.backtop").show();
		}
	});
	$(document).mousedown(function(e){
		var lastY;
		$("body").css("cursor", "pointer").css("user-select", "none");
		$(document).mousemove(function(e){
			if(!lastY){lastY = e.clientY;}
			var currentY = e.clientY;
			if((currentY-lastY) > 10 || (currentY-lastY)<-10){
				$(window).scrollTop($(window).scrollTop()-(currentY-lastY)*1.5);
			}else{
				$(window).scrollTop($(window).scrollTop()-(currentY-lastY));
			}
			lastY = currentY;
		});
		$(document).mouseup(function(e){
			$(document).off("mouseup");
			$(document).off("mousemove");
			$("body").css("cursor", "default").css("user-select", "auto");
		});
	});
});
