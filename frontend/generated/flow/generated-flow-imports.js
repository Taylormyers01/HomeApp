import '@vaadin/tooltip/theme/lumo/vaadin-tooltip.js';
import '@vaadin/polymer-legacy-adapter/style-modules.js';
import '@vaadin/app-layout/theme/lumo/vaadin-drawer-toggle.js';
import '@vaadin/horizontal-layout/theme/lumo/vaadin-horizontal-layout.js';
import '@vaadin/button/theme/lumo/vaadin-button.js';
import 'Frontend/generated/jar-resources/buttonFunctions.js';
import '@vaadin/vertical-layout/theme/lumo/vaadin-vertical-layout.js';
import '@vaadin/app-layout/theme/lumo/vaadin-app-layout.js';
import '@vaadin/common-frontend/ConnectionIndicator.js';
import '@vaadin/vaadin-lumo-styles/color-global.js';
import '@vaadin/vaadin-lumo-styles/typography-global.js';
import '@vaadin/vaadin-lumo-styles/sizing.js';
import '@vaadin/vaadin-lumo-styles/spacing.js';
import '@vaadin/vaadin-lumo-styles/style.js';
import '@vaadin/vaadin-lumo-styles/vaadin-iconset.js';

const loadOnDemand = (key) => {
  const pending = [];
  if (key === 'b05b48bb3f06fe2bf545a2d50fc45f69f1a2e72f94f3b81a8f294942b94813ee') {
    pending.push(import('./chunks/chunk-1f7021012d3a7f9d153ed917e6109c47b884520ab733e98f84bf6370bd0627f3.js'));
  }
  if (key === '41c3a5db00dae5fae9fafc9fd880c9d2707bf1584a8e08edb8d670c43e275df3') {
    pending.push(import('./chunks/chunk-f487d50f88f0ddad754b044ca604f6543127941b33896d0e582f0482cdc3c7c6.js'));
  }
  if (key === 'f31755302d5dceca99b0ae64262ee5a86b40e1dce76e73a76e17efb2982600dd') {
    pending.push(import('./chunks/chunk-1f7021012d3a7f9d153ed917e6109c47b884520ab733e98f84bf6370bd0627f3.js'));
  }
  if (key === '634f3206223cbce911f9b6ca21a5abbf351c0788fa177886bf73aab9b6adc60c') {
    pending.push(import('./chunks/chunk-5cf82126494d4bc70a3914150763493e52d2d47d1ffea779cdef6f1366e3a285.js'));
  }
  return Promise.all(pending);
}

window.Vaadin = window.Vaadin || {};
window.Vaadin.Flow = window.Vaadin.Flow || {};
window.Vaadin.Flow.loadOnDemand = loadOnDemand;
window.Vaadin.Flow.resetFocus = () => {
 let ae=document.activeElement;
 while(ae&&ae.shadowRoot) ae = ae.shadowRoot.activeElement;
 return !ae || ae.blur() || ae.focus() || true;
}