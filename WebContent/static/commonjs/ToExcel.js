var keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

// private method for UTF-8 encoding
function utf8Encode(string) {
	string = string.replace(/\r\n/g, "\n");
	var utftext = "";
	for (var n = 0; n < string.length; n++) {
		var c = string.charCodeAt(n);
		if (c < 128) {
			utftext += String.fromCharCode(c);
		} else if ((c > 127) && (c < 2048)) {
			utftext += String.fromCharCode((c >> 6) | 192);
			utftext += String.fromCharCode((c & 63) | 128);
		} else {
			utftext += String.fromCharCode((c >> 12) | 224);
			utftext += String.fromCharCode(((c >> 6) & 63) | 128);
			utftext += String.fromCharCode((c & 63) | 128);
		}
	}
	return utftext;
}

// public method for encoding
function encode(input) {
	// alert("input=="+input);
	var output = "";
	var chr1, chr2, chr3, enc1, enc2, enc3, enc4;
	var i = 0;
	input = utf8Encode(input);
	while (i < input.length) {
		chr1 = input.charCodeAt(i++);
		chr2 = input.charCodeAt(i++);
		chr3 = input.charCodeAt(i++);
		enc1 = chr1 >> 2;
		enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
		enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
		enc4 = chr3 & 63;
		if (isNaN(chr2)) {
			enc3 = enc4 = 64;
		} else if (isNaN(chr3)) {
			enc4 = 64;
		}
		output = output + keyStr.charAt(enc1) + keyStr.charAt(enc2)
				+ keyStr.charAt(enc3) + keyStr.charAt(enc4);
	}
	return output;
}
Ext.LinkButton = Ext.extend(Ext.Button, {
	template : new Ext.Template(
			'<table border="0" cellpadding="0" cellspacing="0" class="x-btn-wrap"><tbody><tr>',
			'<td class="x-btn-left"><i> </i></td><td class="x-btn-center"><a class="x-btn-text" href="{1}" target="{2}">{0}</a></td><td class="x-btn-right"><i> </i></td>',
			"</tr></tbody></table>"),

	onRender : function(ct, position) {
		var btn, targs = [this.text || ' ', this.href, this.target || "_self"];
		if (position) {
			btn = this.template.insertBefore(position, targs, true);
		} else {
			btn = this.template.append(ct, targs, true);
		}
		var btnEl = btn.child("a:first");
		btnEl.on('focus', this.onFocus, this);
		btnEl.on('blur', this.onBlur, this);

		this.initButtonEl(btn, btnEl);
		Ext.ButtonToggleMgr.register(this);
	},

	onClick : function(e) {
		if (e.button != 0) {
			return;
		}
		if (!this.disabled) {
			this.fireEvent("click", this, e);
			if (this.handler) {
				this.handler.call(this.scope || this, this, e);
			}
		}
	}

});

Ext.ux.toExcel = {
	getExcelXml : function(grid, isThereCheckBox) {

		var worksheet = this.createWorksheet(grid, isThereCheckBox);
//		var totalWidth = grid.getView().getHeaderCt().getTotalWidth(grid);
		return '<?xml version="1.0" encoding="utf-8"?>'
				+ '<ss:Workbook xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns:o="urn:schemas-microsoft-com:office:office">'
				+ '<o:DocumentProperties><o:Title>'
				+ '导出结果数据'
				+ '</o:Title></o:DocumentProperties>'
				+ '<ss:ExcelWorkbook>'
				+ '<ss:WindowHeight>'
				+ worksheet.height
				+ '</ss:WindowHeight>'
				+ '<ss:WindowWidth>'
				+ worksheet.width
				+ '</ss:WindowWidth>'
				+ '<ss:ProtectStructure>False</ss:ProtectStructure>'
				+ '<ss:ProtectWindows>False</ss:ProtectWindows>'
				+ '</ss:ExcelWorkbook>'
				+ '<ss:Styles>'
				+ '<ss:Style ss:ID="Default">'
				+ '<ss:Alignment ss:Vertical="Top" ss:WrapText="1" />'
				+ '<ss:Font ss:FontName="arial" ss:Size="10" />'
				+ '<ss:Borders>'
				+ '<ss:Border ss:Color="#e4e4e4" ss:Weight="1" ss:LineStyle="Continuous" ss:Position="Top" />'
				+ '<ss:Border ss:Color="#e4e4e4" ss:Weight="1" ss:LineStyle="Continuous" ss:Position="Bottom" />'
				+ '<ss:Border ss:Color="#e4e4e4" ss:Weight="1" ss:LineStyle="Continuous" ss:Position="Left" />'
				+ '<ss:Border ss:Color="#e4e4e4" ss:Weight="1" ss:LineStyle="Continuous" ss:Position="Right" />'
				+ '</ss:Borders>'
				+ '<ss:Interior />'
				+ '<ss:NumberFormat />'
				+ '<ss:Protection />'
				+ '</ss:Style>'
				+ '<ss:Style ss:ID="title">'
				+ '<ss:Borders />'
				+ '<ss:Font />'
				+ '<ss:Alignment ss:WrapText="1" ss:Vertical="Center" ss:Horizontal="Center" />'
				+ '<ss:NumberFormat ss:Format="@" />' + '</ss:Style>'
				+ '<ss:Style ss:ID="headercell">'
				+ '<ss:Font ss:Bold="1" ss:Size="10" />'
				+ '<ss:Alignment ss:WrapText="1" ss:Horizontal="Center" />'
				+ '<ss:Interior ss:Pattern="Solid" ss:Color="#A3C9F1" />'
				+ '</ss:Style>' + '<ss:Style ss:ID="even">'
				+ '<ss:Interior ss:Pattern="Solid" ss:Color="#CCFFFF" />'
				+ '</ss:Style>'
				+ '<ss:Style ss:Parent="even" ss:ID="evendate">'
				+ '<ss:NumberFormat ss:Format="[ENG][$-409]dd\-mmm\-yyyy;@" />'
				+ '</ss:Style>' + '<ss:Style ss:Parent="even" ss:ID="evenint">'
				+ '<ss:NumberFormat ss:Format="0" />' + '</ss:Style>'
				+ '<ss:Style ss:Parent="even" ss:ID="evenfloat">'
				+ '<ss:NumberFormat ss:Format="0.00" />' + '</ss:Style>'
				+ '<ss:Style ss:ID="odd">'
				+ '<ss:Interior ss:Pattern="Solid" ss:Color="#CCCCFF" />'
				+ '</ss:Style>' + '<ss:Style ss:Parent="odd" ss:ID="odddate">'
				+ '<ss:NumberFormat ss:Format="[ENG][$-409]dd\-mmm\-yyyy;@" />'
				+ '</ss:Style>' + '<ss:Style ss:Parent="odd" ss:ID="oddint">'
				+ '<ss:NumberFormat ss:Format="0" />' + '</ss:Style>'
				+ '<ss:Style ss:Parent="odd" ss:ID="oddfloat">'
				+ '<ss:NumberFormat ss:Format="0.00" />' + '</ss:Style>'
				+ '</ss:Styles>' + worksheet.xml + '</ss:Workbook>';
	},

	createWorksheet : function(grid, haveCheckBox) {
		// Calculate cell data types and extra class names which affect
		// formatting
		var cellType = [];
		var cellTypeClass = [];
		var cm = grid.getView().getHeaderCt();
		var selectBoxModel = grid.getSelectionModel();
		var isThereCheckBox = false;
		var totalWidthInPixels = 0;
		var colXml = '';
		var headerXml = '';
		// 判断时候含有复选框
		// alert(selectBoxModel);
		if (typeof(selectBoxModel) != 'undefined') {
			isThereCheckBox = true;
		}
		isThereCheckBox = haveCheckBox;
		// alert("isThereCheckBox=="+isThereCheckBox);
		// alert(cm.getColumnCount());
		for (var i = 0; i < cm.getColumnCount(); i++) {
			// alert(cm.isHidden(i));
			if (i == 0 && isThereCheckBox) {
				continue;
			}
			if (!cm.isHidden(i) ) {//&& cm.getHeaderAtIndex(i).length > 0) {
				 //alert(cm.getHeaderAtIndex(i)+"==="+cm.getHeaderAtIndex(i).length);
				 var temp=cm.getHeaderAtIndex(i);
				var w = temp.width;
				totalWidthInPixels += w;
				colXml += '<ss:Column ss:AutoFitWidth="1" ss:Width="' + w
						+ '" />';
				headerXml += '<ss:Cell ss:StyleID="headercell">'
						+ '<ss:Data ss:Type="String">' + temp.text
						+ '</ss:Data>'
						+ '<ss:NamedCell ss:Name="Print_Titles" /></ss:Cell>';
//				var fld = grid.store.recordType.prototype.fields.get(cm
//						.getDataIndex(i));
//				if (typeof(fld) != 'undefined') {
//					switch (fld.type) {
//						case "int" :
//							cellType.push("Number");
//							cellTypeClass.push("int");
//							break;
//						case "float" :
//							cellType.push("Number");
//							cellTypeClass.push("float");
//							break;
//						case "bool" :
//						case "boolean" :
//							cellType.push("String");
//							cellTypeClass.push("");
//							break;
//						case "date" :
//							cellType.push("DateTime");
//							cellTypeClass.push("date");
//							break;
//						default :
//							cellType.push("String");
//							cellTypeClass.push("");
//							break;
//					}
//				} else {
					cellType.push("String");
					cellTypeClass.push("");
//				}
			}
		}
		// alert(cellType.length);
		var visibleColumnCount = cellType.length;

		var result = {
			height : 9000,
			width : Math.floor(totalWidthInPixels * 30) + 50
		};

		// 创建表头信息
		var t = '<ss:Worksheet ss:Name="'
				+ "导出结果数据"
				+ '">'
				+ '<ss:Names>'
				+ '<ss:NamedRange ss:Name="Print_Titles" ss:RefersTo="=\''
				+ "导出结果数据"
				+ '\'!R1:R2" />'
				+ '</ss:Names>'
				+ '<ss:Table x:FullRows="1" x:FullColumns="1"'
				+ ' ss:ExpandedColumnCount="'
				+ visibleColumnCount
				+ '" ss:ExpandedRowCount="'
				+ (grid.store.getCount() + 2)
				+ '">'
				+ colXml
				+ '<ss:Row ss:Height="38">'
				+ '<ss:Cell ss:StyleID="title" ss:MergeAcross="'
				+ (visibleColumnCount - 1)
				+ '">'
				+ '<ss:Data xmlns:html="http://www.w3.org/TR/REC-html40" ss:Type="String">'
				+ '<html:B><html:U><html:Font html:Size="15">'
				+ "导出结果数据"
				+ '</html:Font></html:U></html:B></ss:Data><ss:NamedCell ss:Name="Print_Titles" />'
				+ '</ss:Cell>' + '</ss:Row>' + '<ss:Row ss:AutoFitHeight="1">'
				+ headerXml + '</ss:Row>';

		// 创建记录数据
		var buf=[];
		buf.push(t);
		for (var i = 0, it = grid.store.data.items, l = it.length; i < l; i++) {
			// alert("1111");
			if (isThereCheckBox) {
				var isCheck = true;
				// 选中的记录
				isCheck = selectBoxModel.isSelected(i);
				// alert("是否被选中"+isCheck);
				if (!isCheck) {
					continue;
				}
			}

			buf.push('<ss:Row>') ;
			var cellClass = (i & 1) ? 'odd' : 'even';
			r = it[i].data;
			var k = 0;
			for (var j = 0; j < cm.getColumnCount(); j++) {
				var temp=cm.getHeaderAtIndex(i);
				if (j == 0 && isThereCheckBox) {
					continue;
				}
				// alert(cm.isHidden(3));
				/* if(j!=0){ */

				if (!cm.isHidden(j)) {// && cm.getHeaderAtIndex(j).length > 0) {
					// alert(cm.getHeaderAtIndex(j));
					var v = r[cm.items.items[j].dataIndex];
					//alert(v);
					buf.push( '<ss:Cell ss:StyleID="',cellClass ,cellTypeClass[k],'"><ss:Data ss:Type="' , cellType[k] ,'">');

					if (cellType[k] == 'DateTime') {
						buf.push(v.format('Y-m-d')) ;
					} else {
						if(typeof(v)!='undefined'){
							if(v!=null){
								buf.push(v);
							}else{
								buf.push("");
							}
						}else{
							buf.push(i+1);
						}
					}
					
					buf.push('</ss:Data></ss:Cell>');
					
					k++;
				}

			}
			buf.push('</ss:Row>') ;
		}

		result.xml = buf.join("")
				+ '</ss:Table>'
				+ '<x:WorksheetOptions>'
				+ '<x:PageSetup>'
				+ '<x:Layout x:CenterHorizontal="1" x:Orientation="Landscape" />'
				+ '<x:Footer x:Data="Page &amp;P of &amp;N" x:Margin="0.5" />'
				+ '<x:PageMargins x:Top="0.5" x:Right="0.5" x:Left="0.5" x:Bottom="0.8" />'
				+ '</x:PageSetup>' + '<x:FitToPage />' + '<x:Print>'
				+ '<x:PrintErrors>Blank</x:PrintErrors>'
				+ '<x:FitWidth>1</x:FitWidth>'
				+ '<x:FitHeight>32767</x:FitHeight>' + '<x:ValidPrinterInfo />'
				+ '<x:VerticalResolution>600</x:VerticalResolution>'
				+ '</x:Print>' + '<x:Selected />'
				+ '<x:DoNotDisplayGridlines />'
				+ '<x:ProtectObjects>False</x:ProtectObjects>'
				+ '<x:ProtectScenarios>False</x:ProtectScenarios>'
				+ '</x:WorksheetOptions>' + '</ss:Worksheet>';
		// alert(result.xml);
		return result;
	}

	,
	getAllRsXml : function(grid, isThereCheckBox) {
		// add export all rs method , update by suan
		var worksheet = this.createWorkSheetForAllRs(grid, isThereCheckBox);
//		var totalWidth = grid.getView().getHeaderCt().getTotalWidth(grid);
		return '<?xml version="1.0" encoding="utf-8"?>'
				+ '<ss:Workbook xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns:o="urn:schemas-microsoft-com:office:office">'
				+ '<o:DocumentProperties><o:Title>'
				+ '导出结果数据'
				+ '</o:Title></o:DocumentProperties>'
				+ '<ss:ExcelWorkbook>'
				+ '<ss:WindowHeight>'
				+ worksheet.height
				+ '</ss:WindowHeight>'
				+ '<ss:WindowWidth>'
				+ worksheet.width
				+ '</ss:WindowWidth>'
				+ '<ss:ProtectStructure>False</ss:ProtectStructure>'
				+ '<ss:ProtectWindows>False</ss:ProtectWindows>'
				+ '</ss:ExcelWorkbook>'
				+ '<ss:Styles>'
				+ '<ss:Style ss:ID="Default">'
				+ '<ss:Alignment ss:Vertical="Top" ss:WrapText="1" />'
				+ '<ss:Font ss:FontName="arial" ss:Size="10" />'
				+ '<ss:Borders>'
				+ '<ss:Border ss:Color="#e4e4e4" ss:Weight="1" ss:LineStyle="Continuous" ss:Position="Top" />'
				+ '<ss:Border ss:Color="#e4e4e4" ss:Weight="1" ss:LineStyle="Continuous" ss:Position="Bottom" />'
				+ '<ss:Border ss:Color="#e4e4e4" ss:Weight="1" ss:LineStyle="Continuous" ss:Position="Left" />'
				+ '<ss:Border ss:Color="#e4e4e4" ss:Weight="1" ss:LineStyle="Continuous" ss:Position="Right" />'
				+ '</ss:Borders>'
				+ '<ss:Interior />'
				+ '<ss:NumberFormat />'
				+ '<ss:Protection />'
				+ '</ss:Style>'
				+ '<ss:Style ss:ID="title">'
				+ '<ss:Borders />'
				+ '<ss:Font />'
				+ '<ss:Alignment ss:WrapText="1" ss:Vertical="Center" ss:Horizontal="Center" />'
				+ '<ss:NumberFormat ss:Format="@" />' + '</ss:Style>'
				+ '<ss:Style ss:ID="headercell">'
				+ '<ss:Font ss:Bold="1" ss:Size="10" />'
				+ '<ss:Alignment ss:WrapText="1" ss:Horizontal="Center" />'
				+ '<ss:Interior ss:Pattern="Solid" ss:Color="#A3C9F1" />'
				+ '</ss:Style>' + '<ss:Style ss:ID="even">'
				+ '<ss:Interior ss:Pattern="Solid" ss:Color="#CCFFFF" />'
				+ '</ss:Style>'
				+ '<ss:Style ss:Parent="even" ss:ID="evendate">'
				+ '<ss:NumberFormat ss:Format="[ENG][$-409]dd\-mmm\-yyyy;@" />'
				+ '</ss:Style>' + '<ss:Style ss:Parent="even" ss:ID="evenint">'
				+ '<ss:NumberFormat ss:Format="0" />' + '</ss:Style>'
				+ '<ss:Style ss:Parent="even" ss:ID="evenfloat">'
				+ '<ss:NumberFormat ss:Format="0.00" />' + '</ss:Style>'
				+ '<ss:Style ss:ID="odd">'
				+ '<ss:Interior ss:Pattern="Solid" ss:Color="#CCCCFF" />'
				+ '</ss:Style>' + '<ss:Style ss:Parent="odd" ss:ID="odddate">'
				+ '<ss:NumberFormat ss:Format="[ENG][$-409]dd\-mmm\-yyyy;@" />'
				+ '</ss:Style>' + '<ss:Style ss:Parent="odd" ss:ID="oddint">'
				+ '<ss:NumberFormat ss:Format="0" />' + '</ss:Style>'
				+ '<ss:Style ss:Parent="odd" ss:ID="oddfloat">'
				+ '<ss:NumberFormat ss:Format="0.00" />' + '</ss:Style>'
				+ '</ss:Styles>' + worksheet.xml + '</ss:Workbook>';
	},

	createWorkSheetForAllRs : function(grid, isThereCheckBox) {
		// Calculate cell data types and extra class names which affect
		// formatting
		var cellType = [];
		var cellTypeClass = [];
		var cm = grid.getView().getHeaderCt();
		// var selectBoxModel = grid.getSelectionModel();
		var totalWidthInPixels = 0;
		var colXml = '';
		var headerXml = '';

		for (var i = 0; i < cm.getColumnCount(); i++) {
			if (i == 0 && isThereCheckBox) {
				continue;
			}
			if (!cm.isHidden(i) && cm.getHeaderAtIndex(i).length > 0) {
				// alert(cm.getHeaderAtIndex(i)+"==="+cm.getHeaderAtIndex(i).length);
				var w = cm.getColumnWidth(i)
				totalWidthInPixels += w;
				colXml += '<ss:Column ss:AutoFitWidth="1" ss:Width="' + w
						+ '" />';
				headerXml += '<ss:Cell ss:StyleID="headercell">'
						+ '<ss:Data ss:Type="String">' + cm.getHeaderAtIndex(i)
						+ '</ss:Data>'
						+ '<ss:NamedCell ss:Name="Print_Titles" /></ss:Cell>';
				var fld = grid.store.recordType.prototype.fields.get(cm
						.getDataIndex(i));
				if (typeof(fld) != 'undefined') {
					switch (fld.type) {
						case "int" :
							cellType.push("Number");
							cellTypeClass.push("int");
							break;
						case "float" :
							cellType.push("Number");
							cellTypeClass.push("float");
							break;
						case "bool" :
						case "boolean" :
							cellType.push("String");
							cellTypeClass.push("");
							break;
						case "date" :
							cellType.push("DateTime");
							cellTypeClass.push("date");
							break;
						default :
							cellType.push("String");
							cellTypeClass.push("");
							break;
					}
				} else {
					cellType.push("undefined");
					cellTypeClass.push("");
				}
			}
		}
		// alert(cellType.length);
		var visibleColumnCount = cellType.length;

		var result = {
			height : 9000,
			width : Math.floor(totalWidthInPixels * 30) + 50
		};

		// 创建表头信息
		var t = '<ss:Worksheet ss:Name="'
				+ "导出结果数据"
				+ '">'
				+ '<ss:Names>'
				+ '<ss:NamedRange ss:Name="Print_Titles" ss:RefersTo="=\''
				+ "导出结果数据"
				+ '\'!R1:R2" />'
				+ '</ss:Names>'
				+ '<ss:Table x:FullRows="1" x:FullColumns="1"'
				+ ' ss:ExpandedColumnCount="'
				+ visibleColumnCount
				+ '" ss:ExpandedRowCount="'
				+ (grid.store.getCount() + 2)
				+ '">'
				+ colXml
				+ '<ss:Row ss:Height="38">'
				+ '<ss:Cell ss:StyleID="title" ss:MergeAcross="'
				+ (visibleColumnCount - 1)
				+ '">'
				+ '<ss:Data xmlns:html="http://www.w3.org/TR/REC-html40" ss:Type="String">'
				+ '<html:B><html:U><html:Font html:Size="15">'
				+ "导出结果数据"
				+ '</html:Font></html:U></html:B></ss:Data><ss:NamedCell ss:Name="Print_Titles" />'
				+ '</ss:Cell>' + '</ss:Row>' + '<ss:Row ss:AutoFitHeight="1">'
				+ headerXml + '</ss:Row>';

		// 创建记录数据
		for (var i = 0, it = grid.store.data.items, l = it.length; i < l; i++) {

			t += '<ss:Row>';
			var cellClass = (i & 1) ? 'odd' : 'even';
			r = it[i].data;
			var k = 0;
			for (var j = 0; j < cm.getColumnCount(); j++) {

				if (j == 0 && isThereCheckBox) {
					continue;
				}

				if (!cm.isHidden(j) && cm.getHeaderAtIndex(j).length > 0) {
					// alert(cm.getHeaderAtIndex(j));
					var v = r[cm.getDataIndex(j)];
					t += '<ss:Cell ss:StyleID="' + cellClass + cellTypeClass[k]
							+ '"><ss:Data ss:Type="' + cellType[k] + '">';

					if (cellType[k] == 'DateTime') {
						t += v.format('Y-m-d');
					} else {
						t += v;
					}
					t += '</ss:Data></ss:Cell>';
					k++;
				}

			}
			t += '</ss:Row>';
		}

		result.xml = t
				+ '</ss:Table>'
				+ '<x:WorksheetOptions>'
				+ '<x:PageSetup>'
				+ '<x:Layout x:CenterHorizontal="1" x:Orientation="Landscape" />'
				+ '<x:Footer x:Data="Page &amp;P of &amp;N" x:Margin="0.5" />'
				+ '<x:PageMargins x:Top="0.5" x:Right="0.5" x:Left="0.5" x:Bottom="0.8" />'
				+ '</x:PageSetup>' + '<x:FitToPage />' + '<x:Print>'
				+ '<x:PrintErrors>Blank</x:PrintErrors>'
				+ '<x:FitWidth>1</x:FitWidth>'
				+ '<x:FitHeight>32767</x:FitHeight>' + '<x:ValidPrinterInfo />'
				+ '<x:VerticalResolution>600</x:VerticalResolution>'
				+ '</x:Print>' + '<x:Selected />'
				+ '<x:DoNotDisplayGridlines />'
				+ '<x:ProtectObjects>False</x:ProtectObjects>'
				+ '<x:ProtectScenarios>False</x:ProtectScenarios>'
				+ '</x:WorksheetOptions>' + '</ss:Worksheet>';
		// alert(result.xml);
		return result;
	}
}