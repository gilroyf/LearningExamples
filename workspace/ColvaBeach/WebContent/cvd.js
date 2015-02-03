/**
 * 
 */

var main = function() {

	$(".nav li").click(function () {
		$(".ouput").html("<p>"+$(this)+"</p>");
		console.log("in click");
		console.log(this.textContent);
		
		var makeInactive = function(x) {
			if ($(x).hasClass("active")) {
				$(x).removeClass("active");
				$(x).addClass("inactive");
			}
		}
		
		var makeActive = function(x) {
			if ($(x).hasClass("inactive")) {
				$(x).removeClass("inactive");
				$(x).addClass("active");
			}
		}
		
		if (this.textContent === "Home") {
			makeActive(".home-container");
			makeInactive(".pictures-container2");
			makeInactive(".contact-container");
		} else if (this.textContent === "Pictures") {
			makeInactive(".home-container");
			makeActive(".pictures-container2");
			makeInactive(".contact-container");
		} else if (this.textContent === "Contact Us") {
			makeInactive(".home-container");
			makeInactive(".pictures-container2");
			makeActive(".contact-container");
		}
	});
	
	/* initialize slick stuff */
	$('.pictures-container2').slick({accessiblity:true,fade:true,centerMode:true, speed:600, infinite:true, slideToShow:3, autoplay:true, autoplaySpeed:2600 });
	
}

$(document).ready(main);