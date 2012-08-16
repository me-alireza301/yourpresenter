function onStart() {
	setUp();
	queryData();
}

function setUp() {
	// handle blank/live
	if (isBlank || !isLive) {
		jQuery(".blank").show();
	} else {
		jQuery(".blank").hide();
	}

	// set fg/bg visibility
	jQuery(".active-fg").show();
	fgClassDisplay = jQuery(".active-fg").css('display');
	jQuery(".inactive-fg").hide();
	jQuery(".active-bg").show();
	bgClassDisplay = jQuery(".active-bg").css('display');
	jQuery(".inactive-bg").hide();

	// handle clear
	if (isClear) {
		jQuery(".template-fg").hide();
	} else {
		jQuery(".template-fg").show();
	}
}

function queryData() {
	jQuery.getJSON("../mvc/state/" + scheduleName,
		function(newData) {
			processData(newData);
		});
}

function processData(newData) {
	// TODO JSON comparison, see:
	// http://stackoverflow.com/questions/201183/how-do-you-determine-equality-for-two-javascript-objects
	/*
	 * alert(jQuery.JSON.encode(newData)===jQuery.JSON.encode(data));
	 *  // in case nothing changed => do nothing if
	 * (newData == data) { alert("no update"); return; }
	 */

	var newTxt = newData.actualSlide.text;
	var newIsBlank = newData.schedule.blank;
	var newIsClear = newData.schedule.clear;
	var newIsLive = newData.schedule.live;
	var newBgId = -1;

	if (newData.actualSlide.bgImage != null) {
		newBgId = newData.actualSlide.bgImage.id
	}

	handleBlankAndLive(newIsBlank, newIsLive);
	handleClear(newIsClear);
	handleBgAndFg(newBgId, newTxt, isClear);

	/*data = newData;*/
}

function handleBlankAndLive(newIsBlank, newIsLive) {
	// blank and live not toggled => do nothing
	if ((newIsBlank == isBlank) && (newIsLive == isLive)) {
		return;
	}

	if (newIsBlank || !newIsLive) {
		jQuery(".blank").fadeIn(transitionDuration);
	} else {
		jQuery(".blank").fadeOut(transitionDuration);
	}
	
	isBlank = newIsBlank;
	isLive = newIsLive;
}

function handleClear(newIsClear) {
	// clear not toggled => do nothing
	if (newIsClear == isClear) {
		return;
	}

	if (newIsClear) {
		jQuery(".template-fg").fadeOut(transitionDuration);
	} else {
		jQuery(".template-fg").fadeIn(transitionDuration);
	}

	isClear = newIsClear;
}

function handleBgAndFg(newBgId, newTxt, isClear) {
	// neither txt nor bg changed => do nothing
	if ((newTxt == txt) && (newBgId == bgId)) {
		return;
	}
	
	if (newBgId != bgId && newBgId != -1) {
		var imgPath = "/yourpresenter/mvc/image/" + newBgId;

		/*
		 * preload bg image first, fade it in only afterwards see:
		 * http://stackoverflow.com/questions/5365509/calling-a-function-when-the-background-image-finishes-loading
		 */
		var img = new Image();
		img.onload = function() {
			animate(newBgId, newTxt, isClear, imgPath);
		};
		img.src = imgPath;
	} else {
		animate(newBgId, newTxt, isClear, "");
	}
}

//////////////////////////////////////////////////
// [BEGIN] functions called in animation's step //
//////////////////////////////////////////////////
var fadeOutOldFgF = function fadeOutOldFg(inOpacity) {
	// handle old txt fadeOut()
	jQuery(".active-fg").css("opacity", 1 - inOpacity);
};

var fadeInFgF = function fadeInFg(inOpacity) {
	// handle new bg fadeIn()
	jQuery(".inactive-fg").css("opacity", inOpacity);
};

var fadeOutOldBgF = function fadeOutOldBg(inOpacity) {
	// handle old bg fadeOut()
	jQuery(".active-bg").css("opacity", 1 - inOpacity);
};

var fadeInBgF = function fadeInBg(inOpacity) {
	// handle new bg fadeIn()
	jQuery(".inactive-bg").css("opacity", inOpacity);
};

var emptyF = function empty(inOpacity) {
	// nothing to be done here
};

///////////////////////////////////////////////
//[END] functions called in animation's step //
///////////////////////////////////////////////

function animate(newBgId, newTxt, isClear, imgPath) {
	if (newBgId != bgId) {
		prepareBg(newBgId, imgPath);
	}
	
	if (newTxt != txt) {
		prepareFg(newTxt);
	}
	
	var fadeInFgFunction, fadeInBgFunction, fadeOutFgFunction, fadeOutBgFunction;
	
	if (newTxt != txt && !isClear) {
		if (newTxt != "") {
			fadeInFgFunction = fadeInFgF;
		} else {
			fadeInFgFunction = emptyF
		}
		
		if (txt != "") {
			fadeOutFgFunction = fadeOutOldFgF;
		} else {
			fadeOutFgFunction = emptyF
		}
	} else {
		fadeInFgFunction = emptyF;
		fadeOutFgFunction = emptyF;
	}
	
	if (newBgId != bgId) {
		if (bgId == -1) {
			fadeOutBgFunction = emptyF;
		} else {
			fadeOutBgFunction = fadeOutOldBgF;
		}
		
		if (newBgId == -1) {
			fadeInBgFunction = emptyF;
		} else {
			fadeInBgFunction = fadeInBgF;
			// make sure we don't run 2 image transitions in parallel
			// just cover the old one with the new one
			fadeOutBgFunction = emptyF;
		}
	} else {
		fadeOutBgFunction = emptyF;
		fadeInBgFunction = emptyF;				
	}
	
	animateTemplate(fadeInFgFunction, fadeInBgFunction, fadeOutFgFunction, fadeOutBgFunction, newBgId, newTxt, isClear);
}

function animateTemplate(fadeInFgFunction, fadeInBgFunction, fadeOutFgFunction, fadeOutBgFunction, newBgId, newTxt, isClear) {
jQuery(".placebo")
	.css('opacity', 0.0)
	.animate(
	{
		opacity: 1.0,
	},
	{
		duration: transitionDuration, 
		step: function(now, fx) {
			fadeInFgFunction(now);
			fadeInBgFunction(now);
			fadeOutFgFunction(now);
			fadeOutBgFunction(now);
		},
		complete: function() {
			finish(newBgId, newTxt, isClear);
	    }
	});
}

/*
 * once the opacity == 0.0 => move back and hide()
 */
function finish(newBgId, newTxt, isClear) {
	if (newTxt != txt) {
		jQuery(".active-fg")
			.css("z-index", zIdxBehindAll)
			.hide();
	}
	
	if (newBgId != bgId) {
		jQuery(".active-bg")
			.css("z-index", zIdxBehindAll)
			.hide(
				function() {
					jQuery(".inactive-bg").css("z-index", zIdxBg);
					
					keepHistory(newTxt, newBgId);
			});
	} else {
		keepHistory(newTxt, newBgId);
	}
}

function prepareBg(newBgId, imgPath) {
	// bg not changed => do nothing
	if (newBgId == bgId) {
		return;
	}
	
	if (newBgId == -1) {
		jQuery(".inactive-bg")
			.css({"background-image" : "none"});
	} else {
		jQuery(".inactive-bg")
			.css({"background-image" : "url('" + imgPath + "')"})
	}
	
	jQuery(".inactive-bg")
		.hide()
		.css("z-index", zIdxBg + 1)
		.css('opacity', 0.0)
		// as hide() is roughly equivalent to calling .css('display', 'none')
		// , except that the value of the display property is saved in jQuery's 
		// data cache so that display can later be restored to its initial value.
		// => caching is done programically here
		.css('display', bgClassDisplay);
}

function prepareFg(newTxt) {
	// txt not changed => do nothing
	if (newTxt == txt) {
		return;
	}
	
	// set new txt
	jQuery(".inactive-fg")
		.css("z-index", zIdxBehindAll)
		// keep all in queue
		.queue(function() {
			jQuery(".inactive-fg").children(".jtextfill").children(
				".jtextfill-inner").html(newTxt);
			jQuery(this).dequeue();
		});

	// align size only for non-empty txt
	if (newTxt != "") {
		jQuery(".inactive-fg")
			.show()
			// keep all in queue
			.queue(function() {
				jQuery(".inactive-fg")
					.children(".jtextfill")
					.textfill({maxFontPixels : preferences['view.font.maxsize.projector']});
				jQuery(this).dequeue();
			});
	}

	// hide new and move it in front
	jQuery(".inactive-fg")
		.hide()
		.css("z-index", zIdxFg)
		.css('opacity', 0.0)
		// as hide() is roughly equivalent to calling .css('display', 'none')
		// , except that the value of the display property is saved in jQuery's 
		// data cache so that display can later be restored to its initial value.
		// => caching is done programically here
		.css('display', fgClassDisplay);
}

function keepHistory(newTxt, newBgId) {
	if (newTxt != txt) {
		txt = newTxt;
		jQuery(".inactive-fg").addClass("active-fg");
		jQuery(".active-fg").not(".inactive-fg").removeClass("active-fg").addClass("inactive-fg");
		jQuery(".active-fg").removeClass("inactive-fg");
	}
	
	if (newBgId != bgId) {
		bgId = newBgId;
		jQuery(".inactive-bg").addClass("active-bg");
		jQuery(".active-bg").not(".inactive-bg").removeClass("active-bg").addClass("inactive-bg");
		jQuery(".active-bg").removeClass("inactive-bg");
	}
}
