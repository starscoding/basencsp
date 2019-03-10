var max_password=16;var min_password=8;function validatePwdLevel_util(a){var b=0;if(a&&a.length>=min_password&&a.length<=max_password){if(/\d+/g.test(a)){b+=1}if(/[a-zA-Z]+/g.test(a)){b+=1}if(/[~!@#$%^&*()_+-=\[\]\\{}|;':",.\/<>?]+/g.test(a)){b+=1}}return b}function reverse_util(c){var a=[];if(c){for(var b=c.length-1;b>=0;b--){a.push(c.charAt(b))}}return a.join("")}function validatePwd_util(p,n,d){if(n==null||n==""||n.length<8||n.length>16){return UE_PWD_NOTICE1+"！"}if(validatePwdLevel_util(n)<3){return UE_PWD_NOTICE2+"！"}if(p){p=p.toLocaleLowerCase()}n=n.toLocaleLowerCase();if(p&&n.indexOf(p)!=-1){return"密码为包含用户名弱口令！"}if(/1[3|4|5|8][0-9]\d{8}/g.test(n)){return"密码为包含手机号码弱口令！"}if(d){d=codefans_net_CC2PY(d).toLocaleLowerCase();var g=d.split("-").join("");var k=d.split("-");var a="";for(var s=0;s<k.length;s++){if(k[s]){a+=k[s].charAt(0)}}k.push(a);k.push(g);for(var t=0;t<k.length;t++){if(k[t]&&n.indexOf(k[t])>-1){return"密码为包含用户名任一汉字全拼或者用户名首字母缩写弱口令！"}}}if(/(19|20)\d{2}(0[1-9]|1[0-2])/g.test(n)||/((01|03|05|07|08|10|12)([0-2][1-9]|3[0-1])|(04|06|09|11)([0-2][1-9]|30)|(02)([0-1][1-9]|2[0-8]))/g.test(n)||/(19|20)\d{2}((01|03|05|07|08|10|12)([0-2][1-9]|3[0-1])|(04|06|09|11)([0-2][1-9]|30)|(02)([0-1][1-9]|2[0-8]))/g.test(n)){return"密码为包含日期弱口令！"}var f=n.replace(/1/g,"l").replace(/0/g,"o").replace(/8/g,"b").replace(/@/g,"a");if(p&&f.indexOf(p)>-1){return"密码为包含形似变换弱口令！"}var e="1234567890-= qwertyuiop[]\\ asdfghjkl;' zxcvbnm,./ 1qaz 2wsx 3edc 4rfv 5tgb 6yhn 7ujm 8ik, 9ol. 0p;/ -[' ]'/ =[;. -pl, 0okm 9ijn 8uhb 7ygv 6tfc 5rdx 4esz 3wa";var r=n.replace(/ /g,"");r=r.replace(/!/g,"1");r=r.replace(/@/g,"2");r=r.replace(/#/g,"3");r=r.replace(/\$/g,"4");r=r.replace(/%/g,"5");r=r.replace(/\^/g,"6");r=r.replace(/&/g,"7");r=r.replace(/\*/g,"8");r=r.replace(/\(/g,"9");r=r.replace(/\)/g,"0");r=r.replace(/_/g,"-");r=r.replace(/\+/g,"=");r=r.replace(/{/g,"[");r=r.replace(/}/g,"]");r=r.replace(/\|/g,"\\");r=r.replace(/:/g,";");r=r.replace(/\"/g,"'");r=r.replace(/\>/g,".");r=r.replace(/\</g,",");r=r.replace(/\?/g,"/");for(var t=0;t<r.length-2;t++){var q=r.substring(t,t+3);if(e.indexOf(q)>-1||e.indexOf(reverse_util(q))>-1){return"密码为包含键盘相邻三个字符弱口令！"}}var m="abcdefghijklmnopqrstuvwxyz";var o="01234567890";var l="~!@#$%^&*()_+-=[]{}|;':\",./<>?";for(var t=0;t<n.length-2;t++){var b=n.substring(t,t+3);var h=reverse_util(b);if(m.indexOf(b)>-1||o.indexOf(b)>-1||l.indexOf(b)>-1||m.indexOf(h)>-1){return"密码为包含连续三个字符弱口令！"}var c=n.charAt(t)+n.charAt(t)+n.charAt(t);if(n.indexOf(c)>-1){return"密码为包含重复三个字符弱口令！"}}return""};