BlazeFaces.widget.FileUpload = BlazeFaces.widget.BaseWidget.extend({

    IMAGE_TYPES: /(\.|\/)(gif|jpe?g|png)$/,

    init: function(cfg) {
        this._super(cfg);
        if(this.cfg.disabled) {
            return;
        }
        
        this.ucfg = {};
        this.form = this.jq.closest('form');
        this.uploadTemplateId = BlazeFaces.escapeClientId(this.cfg.uploadTemplateId);
        this.downloadTemplateId = BlazeFaces.escapeClientId(this.cfg.downloadTemplateId);
        this.uploadTemplate = $(this.uploadTemplateId);
        this.downloadTemplate = $(this.downloadTemplateId);
        this.buttonBar = this.jq.children('.ui-fileupload-buttonbar');
        this.chooseButton = this.buttonBar.children('.ui-fileupload-choose');
        this.uploadButton = this.buttonBar.children('.ui-fileupload-upload');
        this.cancelButton = this.buttonBar.children('.ui-fileupload-cancel');
        this.content = this.jq.children('.ui-fileupload-content');
        this.filesTbody = this.content.find('> table.ui-fileupload-files > tbody');
        this.sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
        this.files = [];
        this.cfg.invalidFileMessage = this.cfg.invalidFileMessage || 'Invalid file type';
        this.cfg.invalidSizeMessage = this.cfg.invalidSizeMessage || 'Invalid file size';
        this.cfg.fileLimitMessage = this.cfg.fileLimitMessage || 'Maximum number of files exceeded';
        this.cfg.messageTemplate = this.cfg.messageTemplate || '{name} {size}';
        this.cfg.previewWidth = this.cfg.previewWidth || 80;
        this.uploadedFileCount = 0;

        this.renderMessages();

        this.bindEvents();

        var $this = this,
        encodedURLfield = this.form.children("input[name='javax.faces.encodedURL']");

        this.ucfg = {
            url: (encodedURLfield.length) ? encodedURLfield.val() : this.form.attr('action'),
            paramName: this.id,
            dataType: 'xml',
            dropZone: (this.cfg.dnd === false) ? null : this.jq,
            uploadTemplateId: this.uploadTemplateId,
            downloadTemplateId: this.downloadTemplateId,
            formData: function() {
                return $this.createPostData();
            },
            beforeSend: function(xhr) {
                xhr.setRequestHeader('Faces-Request', 'partial/ajax');
            },
            start: function(e) {
                if($this.cfg.onstart) {
                    $this.cfg.onstart.call($this);
                }
            },
            add: function(e, data) {
                $this.chooseButton.removeClass('ui-state-hover ui-state-focus');
                if($this.files.length === 0) {
                    $this.enableButton($this.uploadButton);
                    $this.enableButton($this.cancelButton);
                }

                if($this.cfg.fileLimit && ($this.uploadedFileCount + $this.files.length + 1) > $this.cfg.fileLimit) {
                    $this.clearMessages();
                    $this.showMessage({
                        summary: $this.cfg.fileLimitMessage
                    });

                    return;
                }
                
                var file = data.files ? data.files[0] : null;
                if(file) {
                    var validMsg = $this.validate(file);

                    if(validMsg) {
                        $this.showMessage({
                            summary: validMsg,
                            filename: file.name,
                            filesize: file.size
                        });
                    }
                    else {
                        $this.clearMessages();

                        var row = $('<tr></tr>').append('<td class="ui-fileupload-preview"></td>')
                                .append('<td>' + file.name + '</td>')
                                .append('<td>' + $this.formatSize(file.size) + '</td>')
                                .append('<td class="ui-fileupload-progress"></td>')
                                .append('<td><button class="ui-fileupload-cancel ui-button ui-widget ui-state-default ui-corner-all ui-button-icon-only"><span class="ui-button-icon-left ui-icon ui-icon ui-icon-close"></span><span class="ui-button-text">ui-button</span></button></td>')
                                .appendTo($this.filesTbody);

                        //preview
                        if($this.isCanvasSupported() && window.File && window.FileReader && $this.IMAGE_TYPES.test(file.name)) {
                            var imageCanvas = $('<canvas></canvas')
                                                    .appendTo(row.children('td.ui-fileupload-preview')),
                            context = imageCanvas.get(0).getContext('2d'),
                            winURL = window.URL||window.webkitURL,
                            url = winURL.createObjectURL(file),
                            img = new Image();

                            img.onload = function() {
                                var imgWidth = null, imgHeight = null, scale = 1;

                                if($this.cfg.previewWidth > this.width) {
                                    imgWidth = this.width;
                                }
                                else {
                                    imgWidth = $this.cfg.previewWidth;
                                    scale = $this.cfg.previewWidth / this.width;
                                }

                                var imgHeight = parseInt(this.height * scale);

                                imageCanvas.attr({width:imgWidth, height: imgHeight});
                                context.drawImage(img, 0, 0, imgWidth, imgHeight);
                            }

                            img.src = url;
                        }

                        //progress
                        row.children('td.ui-fileupload-progress').append('<div class="ui-progressbar ui-widget ui-widget-content ui-corner-all" role="progressbar" aria-valuemin="0" aria-valuemax="100" aria-valuenow="0"><div class="ui-progressbar-value ui-widget-header ui-corner-left" style="display: none; width: 0%;"></div></div>');

                        file.row = row;

                        file.row.data('filedata', data);

                        $this.files.push(file);

                        if($this.cfg.auto) {
                            $this.upload();
                        }
                    }
                }
            },
            send: function(e, data) {
                if(!window.FormData) {
                    for(var i = 0; i < data.files.length; i++) {
                        var file = data.files[i];

                        file.row.children('.ui-fileupload-progress').find('> .ui-progressbar > .ui-progressbar-value')
                                .addClass('ui-progressbar-value-legacy')
                                .css({
                                    width: '100%',
                                    display: 'block'
                                });
                    }
                }
            },
            fail: function(e, data) {
                if($this.cfg.onerror) {
                    $this.cfg.onerror.call($this);
                }
            },
            progress: function(e, data) {
                if(window.FormData) {
                    var progress = parseInt(data.loaded / data.total * 100, 10);

                    for(var i = 0; i < data.files.length; i++) {
                        var file = data.files[i];

                        file.row.children('.ui-fileupload-progress').find('> .ui-progressbar > .ui-progressbar-value').css({
                            width: progress + '%',
                            display: 'block'
                        });
                    }
                }
            },
            done: function(e, data) {
                $this.uploadedFileCount += data.files.length;
                $this.removeFiles(data.files);

                var xmlDoc = $(data.result.documentElement),
                updates = xmlDoc.find('update');

                for (var i = 0; i < updates.length; i++) {
                    var update = updates.eq(i),
                    id = update.attr('id'),
                    content = update.get(0).childNodes[0].nodeValue;

                    BlazeFaces.ajax.AjaxUtils.updateElement.call(this, id, content);
                }

                BlazeFaces.ajax.AjaxUtils.handleResponse.call(this, xmlDoc);
            },
            always: function(e, data) {
                if($this.cfg.oncomplete) {
                    $this.cfg.oncomplete.call($this);
                }
            }
        };

        this.jq.fileupload(this.ucfg);
    },
            
    bindEvents: function() {
        var $this = this;

        BlazeFaces.skinButton(this.buttonBar.children('.ui-button'));

        this.uploadButton.on('click.fileupload', function(e) {
            $this.disableButton($this.uploadButton);
            $this.disableButton($this.cancelButton);
            $this.disableButton($this.filesTbody.find('> tr > td:last-child').children('.ui-fileupload-cancel'));
            
            $this.upload();

            e.preventDefault();
        });

        this.cancelButton.on('click.fileupload', function(e) {
            $this.clear();
            $this.disableButton($this.uploadButton);
            $this.disableButton($this.cancelButton);

            e.preventDefault();
        });

        this.clearMessageLink.on('click.fileupload', function(e) {
            $this.messageContainer.fadeOut(function() {
                $this.messageList.children().remove();
            });

            e.preventDefault();
        });

        this.rowActionSelector = this.jqId + " .ui-fileupload-files button";
        this.rowCancelActionSelector = this.jqId + " .ui-fileupload-files .ui-fileupload-cancel";
        this.clearMessagesSelector = this.jqId + " .ui-messages .ui-messages-close";

        $(document).off('mouseover.fileupload mouseout.fileupload mousedown.fileupload mouseup.fileupload focus.fileupload blur.fileupload click.fileupload ', this.rowCancelActionSelector)
                .on('mouseover.fileupload', this.rowCancelActionSelector, null, function(e) {
                    $(this).addClass('ui-state-hover');
                })
                .on('mouseout.fileupload', this.rowCancelActionSelector, null, function(e) {
                    $(this).removeClass('ui-state-hover ui-state-active');
                })
                .on('mousedown.fileupload', this.rowCancelActionSelector, null, function(e) {
                    $(this).addClass('ui-state-active').removeClass('ui-state-hover');
                })
                .on('mouseup.fileupload', this.rowCancelActionSelector, null, function(e) {
                    $(this).addClass('ui-state-hover').removeClass('ui-state-active');
                })
                .on('focus.fileupload', this.rowCancelActionSelector, null, function(e) {
                    $(this).addclass('ui-state-focus');
                })
                .on('blur.fileupload', this.rowCancelActionSelector, null, function(e) {
                    $(this).removeClass('ui-state-focus');
                })
                .on('click.fileupload', this.rowCancelActionSelector, null, function(e) {
                    var row = $(this).closest('tr'),
                    removedFile = $this.files.splice(row.index(), 1);
                    removedFile[0].row = null;

                    $this.removeFileRow(row);
                    
                    if($this.files.length === 0) {
                        $this.disableButton($this.uploadButton);
                        $this.disableButton($this.cancelButton);
                    }

                    e.preventDefault();
                });
    },
            
    upload: function() {
        for(var i = 0; i < this.files.length; i++) {
            this.files[i].row.data('filedata').submit();
        }
    },
            
    createPostData: function() {
        var process = this.cfg.process ? this.id + ' ' + this.cfg.process : this.id,
                params = this.form.serializeArray();

        params.push({name: BlazeFaces.PARTIAL_REQUEST_PARAM, value: 'true'});
        params.push({name: BlazeFaces.PARTIAL_PROCESS_PARAM, value: process});
        params.push({name: BlazeFaces.PARTIAL_SOURCE_PARAM, value: this.id});

        if (this.cfg.update) {
            params.push({name: BlazeFaces.PARTIAL_UPDATE_PARAM, value: this.cfg.update});
        }

        return params;
    },
            
    formatSize: function(bytes) {
        if(bytes === undefined)
            return '';

        if (bytes === 0)
            return 'N/A';

        var i = parseInt(Math.floor(Math.log(bytes) / Math.log(1024)));
        if (i === 0)
            return bytes + ' ' + this.sizes[i];
        else
            return (bytes / Math.pow(1024, i)).toFixed(1) + ' ' + this.sizes[i];
    },
            
    removeFiles: function(files) {
        for (var i = 0; i < files.length; i++) {
            this.removeFile(files[i]);
        }
    },
            
    removeFile: function(file) {
        var $this = this;

        this.files = $.grep(this.files, function(value) {
            return (value.name === file.name && value.size === file.size);
        }, true);

        $this.removeFileRow(file.row);
        file.row = null;
    },
            
    removeFileRow: function(row) {
        row.fadeOut(function() {
            $(this).remove();
        });
    },
            
    clear: function() {
        for (var i = 0; i < this.files.length; i++) {
            this.removeFileRow(this.files[i].row);
            this.files[i].row = null;
        }

        this.clearMessages();

        this.files = [];
    },
            
    validate: function(file) {
        if (this.cfg.allowTypes && !(this.cfg.allowTypes.test(file.type) || this.cfg.allowTypes.test(file.name))) {
            return this.cfg.invalidFileMessage;
        }

        if (this.cfg.maxFileSize && file.size > this.cfg.maxFileSize) {
            return this.cfg.invalidSizeMessage;
        }

        return null;
    },
            
    renderMessages: function() {
        var markup = '<div class="ui-messages ui-widget ui-helper-hidden"><div class="ui-messages-error ui-corner-all">' +
                '<a class="ui-messages-close" href="#"><span class="ui-icon ui-icon-close"></span></a>' +
                '<span class="ui-messages-error-icon"></span>' +
                '<ul></ul>' +
                '</div></div>';

        this.messageContainer = $(markup).prependTo(this.content);
        this.messageList = this.messageContainer.find('> .ui-messages-error > ul');
        this.clearMessageLink = this.messageContainer.find('> .ui-messages-error > a.ui-messages-close');
    },
            
    clearMessages: function() {
        this.messageContainer.hide();
        this.messageList.children().remove();
    },
            
    showMessage: function(msg) {
        var summary = msg.summary,
        detail = '';

        if(msg.filename && msg.filesize) {
            detail = this.cfg.messageTemplate.replace('{name}', msg.filename).replace('{size}', this.formatSize(msg.filesize));
        }

        this.messageList.append('<li><span class="ui-messages-error-summary">' + summary + '</span><span class="ui-messages-error-detail">' + detail + '</span></li>');
        this.messageContainer.show();
    },
            
    disableButton: function(btn) {
        btn.prop('disabled', true).addClass('ui-state-disabled').removeClass('ui-state-hover ui-state-active ui-state-focus');
    },
            
    enableButton: function(btn) {
        btn.prop('disabled', false).removeClass('ui-state-disabled');
    },
            
    isCanvasSupported: function() {
        var elem = document.createElement('canvas');
        return !!(elem.getContext && elem.getContext('2d'));
    }
    
});