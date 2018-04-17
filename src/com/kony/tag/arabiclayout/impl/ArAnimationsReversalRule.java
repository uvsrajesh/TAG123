package com.kony.tag.arabiclayout.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.ProjectConstants;


public class ArAnimationsReversalRule extends ArAlignmentBaseRule implements ProjectConstants{
	private ArAnimationsReversalRule() {
		
	}

	public ArAnimationsReversalRule(String formQualifier, TreeSet<String> blackListedWidgetList, ProjectConfig projectConfig, String buildChannel) {
		super(formQualifier,blackListedWidgetList,projectConfig,buildChannel);
	}
	
	private static final String IN_TRANSITION_CONFIG = "inTransitionConfig";
	private static final String OUT_TRANSITION_CONFIG = "outTransitionConfig";
	private static final String FORM_TRANSITION_ANDROID = "formAnimation";
	private static final String FORM_TRANSITION_SPA = "formTransition";
	private static final String POPUP_ANDROID_TRANSITION_KEY = "animation";
	private static final String POPUP_BB_IN_TRANSITION_KEY = "transition";
	private static final String POPUP_BB_OUT_TRANSITION_KEY = "out_transition";
	private static final String POPUP_SPA_TRANSITION_KEY = "popupTransition";
	private static final String POPUP_WINDOWS_IN_TRANSITION_KEY = "inTransition";
	private static final String POPUP_WINDOWS_OUT_TRANSITION_KEY = "outTransition";
	
	private static final String IPHONE_TRANSITION_EFFECT = "transitionEffect";
	private static final String IPHONE_TRANSITION_DIRECTION = "transitionDirection";
	private static final String SPA_LEFT_TRANSITION_VALUE = "leftCenter";
	private static final String SPA_RIGHT_TRANSITION_VALUE = "rightCenter";
	private static final String IPHONE_LEFT_TRANSITION_VALUE = "fromLeft";
	private static final String IPHONE_RIGHT_TRANSITION_VALUE = "fromRight";
	private static final String IPHONE_MOVEIN_TRANSITION_EFFECT = "transitionMoveIn";
	private static final String IPHONE_MOVEOUT_TRANSITION_EFFECT = "transitionMoveOut";
	private static final String IPHONE_FLIP_LEFT_TRANSITION_EFFECT = "transitionFlipLeft";
	private static final String IPHONE_FLIP_RIGHT_TRANSITION_EFFECT = "transitionFlipRight";
	private static final String IPHONE_SWITCH_LEFT_TRANSITION_EFFECT = "transitionSwitchLeft";
	private static final String IPHONE_SWITCH_RIGHT_TRANSITION_EFFECT = "transitionSwitchRight";
	private static final String ANDROID_FROM_LEFT_TRANSITION_VALUE = "2";
	private static final String ANDROID_FROM_RIGHT_TRANSITION_VALUE = "3";
	private static final String ANDROID_TO_LEFT_TRANSITION_VALUE = "5";
	private static final String ANDROID_TO_RIGHT_TRANSITION_VALUE = "4";
	private static final String ANDROID_TOPRIGHT_TRANSITION_VALUE = "7";
	private static final String ANDROID_BOTTOMLEFT_TRANSITION_VALUE = "8";
	private static final String POPUP_SLIDEIN_LEFT_TRANSITION_VALUE = "6";
	private static final String POPUP_SLIDEIN_RIGHT_TRANSITION_VALUE = "7";
	private static final String POPUP_BOTTOM_LEFT_TRANSITION_VALUE = "2";
	private static final String POPUP_TOP_RIGHT_TRANSITION_VALUE = "4";
	private static final String POPUP_BB_LEFT_TRANSITION_VALUE = "3";
	private static final String POPUP_BB_RIGHT_TRANSITION_VALUE = "4";
	private static final String POPUP_WINDOWS_SLIDEIN_VALUE = "slidein";
	private static final String POPUP_WINDOWS_SLIDEOUT_VALUE = "slideout";
	private static final String POPUP_WINDOWS_POPIN_VALUE = "popin";
	private static final String POPUP_WINDOWS_POPOUT_VALUE = "popout";
	
	public List<String> reverseAnimationValues(List<String> formattedLines) {
		List<String> newLines = new ArrayList<String>();
		
		int iphoneTransition = -1;
		int transitionEffect= -1;
		int androidTransition = -1;
		int spaTransition = -1;
		int androidPopupTransition = -1;
		int bbPopupInTransition = -1;
		int bbPopupOutTransition = -1;
		int windowsPopupInTransition = -1;
		int windowsPopupOutTransition = -1;
		int spaPopupTransition = -1;
		int colonIndex = - 1;
		int popupIndex = -1;
		int inTransitionConfig = -1;
		boolean popup  = false;
		
		for (String line : formattedLines) {
			
			if(popupIndex < 0){
				popupIndex = line.indexOf(KEYWORD_KONY_UI_POPUP);
				if(popupIndex < 0){
					popupIndex = line.indexOf(KEYWORD_KONY_UI_FORM);
				} else {
					popup = true;
				}
				if(popupIndex < 0){
					newLines.add(line);
					continue;
				}	
			}
			
			if(inTransitionConfig < 0){
				inTransitionConfig = line.indexOf(IN_TRANSITION_CONFIG);
				newLines.add(line);
				continue;
			}
	    	spaTransition = line.indexOf(FORM_TRANSITION_SPA);
			androidTransition = line.indexOf(FORM_TRANSITION_ANDROID);
			iphoneTransition = line.indexOf(IPHONE_TRANSITION_DIRECTION);
			transitionEffect = line.indexOf(IPHONE_TRANSITION_EFFECT);
			androidPopupTransition = line.indexOf(POPUP_ANDROID_TRANSITION_KEY);
			bbPopupInTransition = line.indexOf(POPUP_BB_IN_TRANSITION_KEY);
			bbPopupOutTransition = line.indexOf(POPUP_BB_OUT_TRANSITION_KEY);
			windowsPopupInTransition = line.indexOf(POPUP_WINDOWS_IN_TRANSITION_KEY);
			windowsPopupOutTransition = line.indexOf(POPUP_WINDOWS_OUT_TRANSITION_KEY);
			spaPopupTransition = line.indexOf(POPUP_SPA_TRANSITION_KEY);
			
			colonIndex = line.indexOf(CHAR_COLON);
			
			if(!popup) {
				if (spaTransition > 0) {
					int spaLeftTransitionValue = line.indexOf(SPA_LEFT_TRANSITION_VALUE);
					int spaRightTransitionValue = line.indexOf(SPA_RIGHT_TRANSITION_VALUE);
					
					if(spaLeftTransitionValue > 0 && spaLeftTransitionValue > spaTransition && spaLeftTransitionValue > colonIndex){
						line = line.replaceAll(SPA_LEFT_TRANSITION_VALUE, SPA_RIGHT_TRANSITION_VALUE);
					}
					
					if(spaRightTransitionValue > 0 && spaRightTransitionValue > spaTransition && spaRightTransitionValue > colonIndex){
						line = line.replaceAll(SPA_RIGHT_TRANSITION_VALUE, SPA_LEFT_TRANSITION_VALUE);
					}
				} else if(iphoneTransition > 0){
					int iphoneLeftTransition = line.indexOf(IPHONE_LEFT_TRANSITION_VALUE);
					int iphoneRightTransition = line.indexOf(IPHONE_RIGHT_TRANSITION_VALUE);
					if(iphoneLeftTransition > 0 && iphoneLeftTransition > iphoneTransition && iphoneLeftTransition > colonIndex){
						line = line.replaceAll(IPHONE_LEFT_TRANSITION_VALUE, IPHONE_RIGHT_TRANSITION_VALUE);
					}
					if(iphoneRightTransition > 0 && iphoneRightTransition > iphoneTransition && iphoneRightTransition > colonIndex){
						line = line.replaceAll(IPHONE_RIGHT_TRANSITION_VALUE, IPHONE_LEFT_TRANSITION_VALUE);
					}
				} else if(transitionEffect > 0){
					/*int moveInLeftTransition = line.indexOf(IPHONE_MOVEIN_TRANSITION_EFFECT);
					int flipLeftTransition = line.indexOf(IPHONE_FLIP_LEFT_TRANSITION_EFFECT);
					int switchLeftTransition = line.indexOf(IPHONE_SWITCH_LEFT_TRANSITION_EFFECT);
					int moveOutTransition = line.indexOf(IPHONE_MOVEOUT_TRANSITION_EFFECT);
					int flipRightTransition = line.indexOf(IPHONE_FLIP_RIGHT_TRANSITION_EFFECT);
					int switchRightTransition = line.indexOf(IPHONE_SWITCH_RIGHT_TRANSITION_EFFECT);
					
					if(moveInLeftTransition > 0 && moveInLeftTransition > transitionEffect && moveInLeftTransition > colonIndex){
						line = line.replaceAll(IPHONE_MOVEIN_TRANSITION_EFFECT, IPHONE_MOVEOUT_TRANSITION_EFFECT);
					} else if(flipLeftTransition > 0 && flipLeftTransition > transitionEffect && flipLeftTransition > colonIndex){
						line = line.replaceAll(IPHONE_FLIP_LEFT_TRANSITION_EFFECT, IPHONE_FLIP_RIGHT_TRANSITION_EFFECT);
					} else if(switchLeftTransition > 0 && switchLeftTransition > transitionEffect && switchLeftTransition > colonIndex){
						line = line.replaceAll(IPHONE_SWITCH_LEFT_TRANSITION_EFFECT, IPHONE_SWITCH_RIGHT_TRANSITION_EFFECT);
					}
					
					if(moveOutTransition > 0 && moveOutTransition > transitionEffect && moveOutTransition > colonIndex){
						line = line.replaceAll(IPHONE_MOVEOUT_TRANSITION_EFFECT, IPHONE_MOVEIN_TRANSITION_EFFECT);
					} else if(flipRightTransition > 0 && flipRightTransition > transitionEffect && flipRightTransition > colonIndex){
						line = line.replaceAll(IPHONE_FLIP_RIGHT_TRANSITION_EFFECT, IPHONE_FLIP_LEFT_TRANSITION_EFFECT);
					} else if(switchRightTransition > 0 && switchRightTransition > transitionEffect && switchRightTransition > colonIndex){
						line = line.replaceAll(IPHONE_SWITCH_RIGHT_TRANSITION_EFFECT, IPHONE_SWITCH_LEFT_TRANSITION_EFFECT);
					}*/
				} else if(androidTransition > 0){
					int androidToLeftTransition = line.indexOf(ANDROID_TO_LEFT_TRANSITION_VALUE);
					int androidFromLeftTransition = line.indexOf(ANDROID_FROM_LEFT_TRANSITION_VALUE);
					int androidBottomLeftTransition = line.indexOf(ANDROID_BOTTOMLEFT_TRANSITION_VALUE);
					int androidToRightTransition = line.indexOf(ANDROID_TO_RIGHT_TRANSITION_VALUE);
					int androidFromRightTransition = line.indexOf(ANDROID_FROM_RIGHT_TRANSITION_VALUE);
					int androidTopRightTransition = line.indexOf(ANDROID_TOPRIGHT_TRANSITION_VALUE);
					
					if(androidToLeftTransition > 0 && androidToLeftTransition > androidTransition && androidToLeftTransition > colonIndex){
						line = line.replaceAll(ANDROID_TO_LEFT_TRANSITION_VALUE, ANDROID_TO_RIGHT_TRANSITION_VALUE);
					} else if(androidFromLeftTransition > 0 && androidFromLeftTransition > androidTransition && androidFromLeftTransition > colonIndex){
						line = line.replaceAll(ANDROID_FROM_LEFT_TRANSITION_VALUE, ANDROID_FROM_RIGHT_TRANSITION_VALUE);
					} else if(androidBottomLeftTransition > 0 && androidBottomLeftTransition > androidTransition && androidBottomLeftTransition > colonIndex){
						line = line.replaceAll(ANDROID_BOTTOMLEFT_TRANSITION_VALUE, ANDROID_TOPRIGHT_TRANSITION_VALUE);
					}
					
					if(androidToRightTransition > 0 && androidToRightTransition > androidTransition && androidToRightTransition > colonIndex){
						line = line.replaceAll(ANDROID_TO_RIGHT_TRANSITION_VALUE, ANDROID_TO_LEFT_TRANSITION_VALUE);
					} else if(androidFromRightTransition > 0 && androidFromRightTransition > androidTransition && androidFromRightTransition > colonIndex){
						line = line.replaceAll(ANDROID_FROM_RIGHT_TRANSITION_VALUE, ANDROID_FROM_LEFT_TRANSITION_VALUE);
					} else if(androidTopRightTransition > 0 && androidTopRightTransition > androidTransition && androidTopRightTransition > colonIndex){
						line = line.replaceAll(ANDROID_TOPRIGHT_TRANSITION_VALUE, ANDROID_BOTTOMLEFT_TRANSITION_VALUE);
					}
				}
			} else {
				if (spaPopupTransition > 0) {
					int spaLeftTransitionValue = line.indexOf(SPA_LEFT_TRANSITION_VALUE);
					int spaRightTransitionValue = line.indexOf(SPA_RIGHT_TRANSITION_VALUE);
					
					if(spaLeftTransitionValue > 0 && spaLeftTransitionValue > spaPopupTransition && spaLeftTransitionValue > colonIndex){
						line = line.replaceAll(SPA_LEFT_TRANSITION_VALUE, SPA_RIGHT_TRANSITION_VALUE);
					}
					if(spaRightTransitionValue > 0 && spaRightTransitionValue > spaPopupTransition && spaRightTransitionValue > colonIndex){
						line = line.replaceAll(SPA_RIGHT_TRANSITION_VALUE, SPA_LEFT_TRANSITION_VALUE);
					}
				} else if(iphoneTransition > 0){
					int iphoneLeftTransition = line.indexOf(IPHONE_LEFT_TRANSITION_VALUE);
					int iphoneRightTransition = line.indexOf(IPHONE_RIGHT_TRANSITION_VALUE);
					
					if(iphoneLeftTransition > 0 && iphoneLeftTransition > iphoneTransition && iphoneLeftTransition > colonIndex){
						line = line.replaceAll(IPHONE_LEFT_TRANSITION_VALUE, IPHONE_RIGHT_TRANSITION_VALUE);
					}
					if(iphoneRightTransition > 0 && iphoneRightTransition > iphoneTransition && iphoneRightTransition > colonIndex){
						line = line.replaceAll(IPHONE_RIGHT_TRANSITION_VALUE, IPHONE_LEFT_TRANSITION_VALUE);
					}
					
				} else if(transitionEffect > 0){
					/*int moveInTransition = line.indexOf(IPHONE_MOVEIN_TRANSITION_EFFECT);
					int moveOutTransition = line.indexOf(IPHONE_MOVEOUT_TRANSITION_EFFECT);
					
					if(moveInTransition > 0 && moveInTransition > transitionEffect && moveInTransition > colonIndex){
						line = line.replaceAll(IPHONE_MOVEIN_TRANSITION_EFFECT, IPHONE_MOVEOUT_TRANSITION_EFFECT);
					}
					if(moveOutTransition > 0 && moveOutTransition > transitionEffect && moveOutTransition > colonIndex){
						line = line.replaceAll(IPHONE_MOVEOUT_TRANSITION_EFFECT, IPHONE_MOVEIN_TRANSITION_EFFECT);
					}*/
				} else if(androidPopupTransition > 0){
					int androidToLeftTransition = line.indexOf(POPUP_SLIDEIN_LEFT_TRANSITION_VALUE);
					int androidBottomLeftTransition = line.indexOf(POPUP_BOTTOM_LEFT_TRANSITION_VALUE);
					int androidToRightTransition = line.indexOf(POPUP_SLIDEIN_RIGHT_TRANSITION_VALUE);
					int androidTopRightTransition = line.indexOf(POPUP_TOP_RIGHT_TRANSITION_VALUE);
					
					if(androidToLeftTransition > 0 && androidToLeftTransition > androidPopupTransition && androidToLeftTransition > colonIndex){
						line = line.replaceAll(POPUP_SLIDEIN_LEFT_TRANSITION_VALUE, POPUP_SLIDEIN_RIGHT_TRANSITION_VALUE);
					}else if(androidBottomLeftTransition > 0 && androidBottomLeftTransition > androidPopupTransition && androidBottomLeftTransition > colonIndex){
						line = line.replaceAll(POPUP_BOTTOM_LEFT_TRANSITION_VALUE, POPUP_TOP_RIGHT_TRANSITION_VALUE);
					}
					
					if(androidToRightTransition > 0 && androidToRightTransition > androidPopupTransition && androidToRightTransition > colonIndex){
						line = line.replaceAll(POPUP_SLIDEIN_RIGHT_TRANSITION_VALUE, POPUP_SLIDEIN_LEFT_TRANSITION_VALUE);
					}else if(androidTopRightTransition > 0 && androidTopRightTransition > androidPopupTransition && androidTopRightTransition > colonIndex){
						line = line.replaceAll(POPUP_TOP_RIGHT_TRANSITION_VALUE, POPUP_BOTTOM_LEFT_TRANSITION_VALUE);
					}
				} else if(bbPopupOutTransition < 0 && bbPopupInTransition > 0){
					int bbLeftTransition = line.indexOf(POPUP_BB_LEFT_TRANSITION_VALUE);
					int bbRightTransition = line.indexOf(POPUP_BB_RIGHT_TRANSITION_VALUE);
					
					if(bbLeftTransition > 0 && bbLeftTransition > bbPopupInTransition && bbLeftTransition > colonIndex){
						line = line.replaceAll(POPUP_BB_LEFT_TRANSITION_VALUE, POPUP_BB_RIGHT_TRANSITION_VALUE);
					}
					if(bbRightTransition > 0 && bbRightTransition > bbPopupInTransition && bbRightTransition > colonIndex){
						line = line.replaceAll(POPUP_BB_RIGHT_TRANSITION_VALUE, POPUP_BB_LEFT_TRANSITION_VALUE);
					}
				} else if(bbPopupOutTransition > 0){
					int bbLeftTransition = line.indexOf(POPUP_BB_LEFT_TRANSITION_VALUE);
					int bbRightTransition = line.indexOf(POPUP_BB_RIGHT_TRANSITION_VALUE);
					
					if(bbLeftTransition > 0 && bbLeftTransition > bbPopupOutTransition && bbLeftTransition > colonIndex){
						line = line.replaceAll(POPUP_BB_LEFT_TRANSITION_VALUE, POPUP_BB_RIGHT_TRANSITION_VALUE);
					}
					if(bbRightTransition > 0 && bbRightTransition > bbPopupOutTransition && bbRightTransition > colonIndex){
						line = line.replaceAll(POPUP_BB_RIGHT_TRANSITION_VALUE, POPUP_BB_LEFT_TRANSITION_VALUE);
					}
				} else if(windowsPopupInTransition > 0){
					int windowsSlideInTransition = line.indexOf(POPUP_WINDOWS_SLIDEIN_VALUE);
					int windowsPopInTransition = line.indexOf(POPUP_WINDOWS_POPIN_VALUE);
					
					if(windowsSlideInTransition > 0 && windowsSlideInTransition > windowsPopupInTransition && windowsSlideInTransition > colonIndex){
						line = line.replaceAll(POPUP_WINDOWS_SLIDEIN_VALUE, POPUP_WINDOWS_SLIDEOUT_VALUE);
					} else if(windowsPopInTransition > 0 && windowsPopInTransition > windowsPopupInTransition && windowsPopInTransition > colonIndex){
						line = line.replaceAll(POPUP_WINDOWS_POPIN_VALUE, POPUP_WINDOWS_POPOUT_VALUE);
					}
					
				} else if(windowsPopupOutTransition > 0){
					int windowsSlideOutTransition = line.indexOf(POPUP_WINDOWS_SLIDEOUT_VALUE);
					int windowsPopOutTransition = line.indexOf(POPUP_WINDOWS_POPOUT_VALUE);
					
					if(windowsSlideOutTransition > 0 && windowsSlideOutTransition > windowsPopupOutTransition && windowsSlideOutTransition > colonIndex){
						line = line.replaceAll(POPUP_WINDOWS_SLIDEOUT_VALUE, POPUP_WINDOWS_SLIDEIN_VALUE);
					} else if(windowsPopOutTransition > 0 && windowsPopOutTransition > windowsPopupOutTransition && windowsPopOutTransition > colonIndex){
						line = line.replaceAll(POPUP_WINDOWS_POPOUT_VALUE, POPUP_WINDOWS_POPIN_VALUE);
					}
				}
			}
			
			newLines.add(line);
		}
		return newLines;
	}
}
	
