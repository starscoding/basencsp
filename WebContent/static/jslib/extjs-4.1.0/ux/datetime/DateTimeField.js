Ext.define('Ext.ux.datetime.DateTimeField', {
	  extend: 'Ext.form.field.Date',
	  alias: 'widget.datetimefield',
	  requires: ['Ext.ux.datetime.DateTimePicker'],

	  initComponent: function() {
		  this.format = this.format + ' ' + 'H:i:s';
		  this.callParent();
	  },
	  // overwrite
	  createPicker: function() {
		  var me = this,
			  format = Ext.String.format;

		  return Ext.create('Ext.ux.datetime.DateTimePicker', {
			    ownerCt: me.ownerCt,
			    renderTo: document.body,
			    floating: true,
			    hidden: true,
			    focusOnShow: true,
			    minDate: me.minValue,
			    maxDate: me.maxValue,
			    disabledDatesRE: me.disabledDatesRE,
			    disabledDatesText: me.disabledDatesText,
			    disabledDays: me.disabledDays,
			    disabledDaysText: me.disabledDaysText,
			    format: me.format,
			    showToday: me.showToday,
			    startDay: me.startDay,
			    minText: format(me.minText, me.formatDate(me.minValue)),
			    maxText: format(me.maxText, me.formatDate(me.maxValue)),
			    listeners: {
				    scope: me,
				    select: me.onSelect
			    },
			    keyNavConfig: {
				    esc: function() {
					    me.collapse();
				    }
			    }
		    });
	  }
  });

  Ext.apply(Ext.form.field.VTypes, {
	  daterange: function(val, field) {
	      var date = field.parseDate(val);
	
	      if (!date) {
	          return false;
	      }
	      if (field.startDateField && (!this.dateRangeMax || (date.getTime() != this.dateRangeMax.getTime()))) {
	          var start = field.up('form').down('#' + field.startDateField);
	          start.setMaxValue(date);
	          start.validate();
	          this.dateRangeMax = date;
	      }
	      else if (field.endDateField && (!this.dateRangeMin || (date.getTime() != this.dateRangeMin.getTime()))) {
	          var end = field.up('form').down('#' + field.endDateField);
	          end.setMinValue(date);
	          end.validate();
	          this.dateRangeMin = date;
	      }
	      return true;
	  },
	  daterangeText: 'Start date must be less than end date',
	  password: function(val, field) {
	      if (field.initialPassField) {
	          var pwd = field.up('form').down('#' + field.initialPassField);
	          return (val == pwd.getValue());
	      }
	      return true;
	  },
	  passwordText: 'Passwords do not match'
	});