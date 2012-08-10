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
	jQuery(fgClass).show();
	jQuery(alterClass(fgClass)).hide();
	jQuery(bgClass).show();
	jQuery(alterClass(bgClass)).hide();

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
	handleBg(newBgId);
	handleFg(newTxt, isClear);

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

function handleFg(newTxt, isClear) {
	// txt not changed => do nothing
	if (newTxt == txt) {
		return;
	}

	var newFgClass = alterClass(fgClass);

	// set new txt
	jQuery(newFgClass).css("z-index", zIdxBehindAll);
	jQuery(newFgClass).children(".jtextfill").children(
			".jtextfill-inner").html(newTxt);

	// align size only for non-empty txt
	if (newTxt != "") {
		jQuery(newFgClass).show();
		jQuery(newFgClass)
				.children(".jtextfill")
				.textfill({maxFontPixels : preferences['view.font.maxsize.projector']});
	}

	// hide new and move it in front
	jQuery(newFgClass)
		.hide()
		.css("z-index", zIdxFg);

	// old fade out + move back
	jQuery(fgClass)
		.fadeOut(transitionDuration)
		.css("z-index", zIdxBehindAll);

	// if to be displayed do the effect
	if (!isClear && newTxt != "") {
		jQuery(newFgClass).fadeIn(transitionDuration);
	}

	txt = newTxt;
	fgClass = newFgClass;
}

function handleBg(newBgId) {
	// bg not changed => do nothing
	if (newBgId == bgId) {
		return;
	}

	var newBgClass = alterClass(bgClass);

	// if new bg is empty
	if (newBgId == -1) {
		jQuery(newBgClass)
			.css({"background-image" : "none"})
			.css("z-index", zIdxBg)
			.show(
				function() {
					// hide old and show new
					jQuery(bgClass).fadeOut(transitionDuration);
					
					bgId = newBgId;
					bgClass = newBgClass;
			});
	} else {
		var imgPath = "/yourpresenter/mvc/image/" + newBgId;

		/*
		 * preload bg image first, fade it in only afterwards see:
		 * http://stackoverflow.com/questions/5365509/calling-a-function-when-the-background-image-finishes-loading
		 */
		var img = new Image();
		img.onload = function() {
			
			//
			// hide old and show new
			//
			jQuery(newBgClass)
				.css({"background-image" : "url('" + imgPath + "')"})
				.css("z-index", zIdxBg + 1)
				.fadeIn(transitionDuration, 
					function() {
						jQuery(bgClass)
							.css("z-index", zIdxBehindAll)
							.hide(
								function() {
									jQuery(newBgClass).css("z-index", zIdxBg);
									
									bgId = newBgId;
									bgClass = newBgClass;	
							});
				});
		};
		img.src = imgPath;
	}
}

function alterClass(oldClass) {
	var newClass = "";
	if (oldClass.substring(0, ".one".length) === ".one") {
		newClass = oldClass.replace("one", "two");
	} else {
		newClass = oldClass.replace("two", "one");
	}
	return newClass;
}
