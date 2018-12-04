/**
 * notificationFx.js v1.0.0
 * http://www.codrops.com
 *
 * Licensed under the MIT license.
 * http://www.opensource.org/licenses/mit-license.php
 * 
 * Copyright 2014, Codrops
 * http://www.codrops.com
 */
;
(function (window) {

    'use strict';

    var docElem = window.document.documentElement,
            support = {animations: Modernizr.cssanimations},
            animEndEventNames = {
                'WebkitAnimation': 'webkitAnimationEnd',
                'OAnimation': 'oAnimationEnd',
                'msAnimation': 'MSAnimationEnd',
                'animation': 'animationend'
            },
            // animation end event name
            animEndEventName = animEndEventNames[ Modernizr.prefixed('animation') ];

    /**
     * extend obj function
     */
    function extend(a, b) {
        for (var key in b) {
            if (b.hasOwnProperty(key)) {
                a[key] = b[key];
            }
        }
        return a;
    }

    /**
     * NotificationFx function
     */
    function NotificationFx(options) {
        this.options = extend({}, this.options);
        extend(this.options, options);
        this._init();
    }

    /**
     * NotificationFx options
     */
    NotificationFx.prototype.options = {

        // element to which the notification will be appended
        // defaults to the document.body
        wrapper: document.body,
        // Color de fondo
        color: '#ffff',
        //Icono
        icono: 'fa fa-home',
        //Nombre
        nombre: 'Notificación',
        //The texto
        texto: 'yo!',
        // if the user doesn´t close the notification then we remove it 
        // after the following time
        ttl: 18000000,
        // callbacks
        onClose: function () {
            return false;
        },
        onOpen: function () {
            return false;
        }
    }

    /**
     * init function
     * initialize and cache some vars
     */
    NotificationFx.prototype._init = function () {
        // create HTML structure
        this.ntf = document.createElement('div');
        this.ntf.className = 'ns-box ns-bar ns-effect-slidetop';
        this.ntf.style = "background-color:" + this.options.color + "";
        var strinner = '<div class="ns-box-inner text-center">';
        strinner += '<h4 class="text-center" style="font-size: 30px"><i class="'+ this.options.icono +'" aria-hidden="true"></i>&ensp;'+ this.options.nombre +'</h4><br>';
        strinner += this.options.texto;
        strinner += '</div>';
        strinner += '<span class="ns-close"></span></div>';
        this.ntf.innerHTML = strinner;

        // append to body or the element specified in options.wrapper
        this.options.wrapper.insertBefore(this.ntf, this.options.wrapper.firstChild);

        // dismiss after [options.ttl]ms
        var self = this;
        this.dismissttl = setTimeout(function () {
            if (self.active) {
                self.dismiss();
            }
        }, this.options.ttl);

        // init events
        this._initEvents();
    }

    /**
     * init events
     */
    NotificationFx.prototype._initEvents = function () {
        var self = this;
        // dismiss notification
        this.ntf.querySelector('.ns-close').addEventListener('click', function () {
            self.dismiss();
        });
    }

    /**
     * show the notification
     */
    NotificationFx.prototype.show = function () {
        this.active = true;
        classie.remove(this.ntf, 'ns-hide');
        classie.add(this.ntf, 'ns-show');
        this.options.onOpen();
    }

    /**
     * dismiss the notification
     */
    NotificationFx.prototype.dismiss = function () {
        var self = this;
        this.active = false;
        clearTimeout(this.dismissttl);
        classie.remove(this.ntf, 'ns-show');
        setTimeout(function () {
            classie.add(self.ntf, 'ns-hide');

            // callback
            self.options.onClose();
        }, 25);

        // after animation ends remove ntf from the DOM
        var onEndAnimationFn = function (ev) {
            if (support.animations) {
                if (ev.target !== self.ntf)
                    return false;
                this.removeEventListener(animEndEventName, onEndAnimationFn);
            }
            self.options.wrapper.removeChild(this);
        };

        if (support.animations) {
            this.ntf.addEventListener(animEndEventName, onEndAnimationFn);
        } else {
            onEndAnimationFn();
        }
    }

    /**
     * add to global namespace
     */
    window.NotificationFx = NotificationFx;

})(window);