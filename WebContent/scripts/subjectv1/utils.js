define(["text!./template/msg.html"],function(a){return{isBlank:function(b){return !this.isNotBlank(b)},isNotBlank:function(b){return b&&b.length},validateAjaxRs:function(c,e){var b=this;if(c&&c.success=="true"){return true}else{if(e&&e.length){var d="";if(e=="data.msg"){d=c?c.msg:"调用失败"}else{d=e}b.showMsg("danger",d)}return false}},getTplDom:function(b,c){return c?$(Mustache.render(b,c)):$(b)},showMsg:function(c,d,b){var e=this.getTplDom(a,{type:c,msg:d});e.hide();$("#msg-cnt").append(e);e.slideDown(500,function(){});if(b===true){e.delay(5000).fadeOut(500,function(){$(this).remove()})}},is:function(d,b,c){if($(d).is(b)){return $(d)}else{if($(d).is(c)){return false}else{if(d.parentNode){return this.is(d.parentNode,b,c)}else{return false}}}}}});