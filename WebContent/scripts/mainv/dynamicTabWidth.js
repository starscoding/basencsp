function GetCurrentStrWidth(c){var a=$("<pre>").hide().appendTo(document.body);$(a).html(c).css("font","normal 14px 'Microsoft YaHei', 黑体, sans-serif");var b=a.width();a.remove();return b}function isAdd(d){var a=getOneWidth(d);var c=a[0];var b=14;if(c<(30+4+b)){return false}else{return true}}function getOneWidth(b){var c=$("#tabs").outerWidth();var f=$("#tabs li:eq(0)").outerWidth(true);var a=0;var h=0;$("#tabs li").each(function(j){h++;a+=$(this).outerWidth(true)});if(h<3){return[c,c]}var i=GetCurrentStrWidth(b.nameCn)+40+4;var g=c-i-f;var e=Math.floor(g/(h-2));var d=c-f-((h-2)*(14+30+4));if(e<(30+4+14)){d=c-f-((h-2)*(14+30+4))}else{d=c-f-((h-2)*(e))}return[e,d]}function dynamicWidthForShow(e){var b=getOneWidth(e);var d=b[0];var a=b[1]-4-20;var c=14;if(d<(30+4+c)){$("#tabs li").each(function(g,f){if(g>0){if(e.nameCn==$(f).attr("title")){$(this).css("width",a+"px");$(this).css("padding-left","10px")}else{$(this).css("width",(c+30)+"px")}}})}else{$("#tabs li").each(function(h,f){if(h>0){var g=GetCurrentStrWidth($(f).attr("title"))+40+4;if(e.nameCn==$(f).attr("title")){if(a<=g&&d<=g){$(this).css("width",a+"px")}else{$(this).css("width","")}}else{if(d>=g){$(this).css("width","");$("#tabs li").each(function(k,j){if(k>0){$(this).css("padding-left","")}})}else{$("#tabs li").each(function(k,j){if(k>0){$(this).css("padding-left","10px")}});$(this).css("width",(d-4)+"px")}}}})}}function dynamicWidthForDestroy(){$("#tabs li").each(function(b,a){if(b>0){if($(this)[0].className==="active"){console.log(22);var c={nameCn:$(this).attr("title")};dynamicWidthForShow(c)}}})};