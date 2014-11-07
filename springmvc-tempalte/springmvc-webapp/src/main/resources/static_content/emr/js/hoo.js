/**
 * JS工具
 * @author YoHoo
 */
(function(global, factory) {
	if (typeof module === "object" && typeof module.exports === "object") {
		module.exports = global.document ? factory(global, true) : function(w) {
			if (!w.document) {
				throw new Error("jQuery requires a window with a document");
			}
			return factory(w);
		};
	} else {
		factory(global);
	}
}(typeof window !== "undefined" ? window : this, function(window, noGlobal) {

	/**
	 * jQuery对象扩展方法
	 */
	$.fn.extend({
		version : function() {
			alert(version);
		}
	});

	/**
	 * 工具方法
	 */
	var version = "1.0", hoo = {
		name : 'Hoo Js',
		alert1 : function() {
			alert(this.name);
		}
	};

	window.hoo = hoo;

	return hoo;
}));