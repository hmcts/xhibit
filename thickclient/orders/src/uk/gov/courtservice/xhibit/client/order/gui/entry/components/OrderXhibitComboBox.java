package uk.gov.courtservice.xhibit.client.order.gui.entry.components;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;

import uk.gov.courtservice.xhibit.client.order.gui.components.CustomXhibitComboBox;
import uk.gov.courtservice.xhibit.client.order.gui.entry.AbstractOrderComponent;
import uk.gov.courtservice.xhibit.client.order.screens.helper.ResourceHelper;

/**
 * Created by IntelliJ IDEA. User: tzj8k5 Date: 22-Mar-2005 Time: 11:21:36 To
 * change this template use File | Settings | File Templates.
 */
public class OrderXhibitComboBox extends AbstractOrderComponent implements ItemListener {

    private static final String ORDER_TYPE_0 = "orders.type.dropdown.value0";

    private static final String ORDER_TYPE_1 = "orders.type.dropdown.value1";

    private static final String ORDER_TYPE_2 = "orders.type.dropdown.value2";

    private static final String ORDER_TYPE_3 = "orders.type.dropdown.value3";

    private static final String ORDER_TYPE_4 = "orders.type.dropdown.value4";

    private static final String ORDER_TYPE_5 = "orders.type.dropdown.value5";

    private static final String ORDER_TYPE_6 = "orders.type.dropdown.value6";

    private static final String ORDER_TYPE_7 = "orders.type.dropdown.value7";

    private static final String ORDER_TYPE_8 = "orders.type.dropdown.value8";

    private static final int TOTAL_ORDERS = 9;

    private static final String PROG_TYPE_0 = "orders.programme.dropdown.value0";

    private static final String PROG_TYPE_1 = "orders.programme.dropdown.value1";

    private static final String PROG_TYPE_2 = "orders.programme.dropdown.value2";

    private static final String PROG_TYPE_3 = "orders.programme.dropdown.value3";

    private static final String PROG_TYPE_4 = "orders.programme.dropdown.value4";

    private static final String PROG_TYPE_5 = "orders.programme.dropdown.value5";

    private static final int TOTAL_PROGRAMMES = 6;

    private static final String RESP_OFFICER1_TYPE_0 = "orders.responsibleOfficer1.dropdown.value0";

    private static final String RESP_OFFICER1_TYPE_1 = "orders.responsibleOfficer1.dropdown.value1";

    private static final String RESP_OFFICER1_TYPE_2 = "orders.responsibleOfficer1.dropdown.value2";

    private static final String RESP_OFFICER1_TYPE_3 = "orders.responsibleOfficer1.dropdown.value3";

    private static final String RESP_OFFICER1_TYPE_4 = "orders.responsibleOfficer1.dropdown.value4";
    
    private static final String RESP_OFFICER1_TYPE_5 = "orders.responsibleOfficer1.dropdown.value5";

    private static final int TOTAL_RESP_OFFICER1 = 6;

    private static final String RESP_OFFICER2_TYPE_0 = "orders.responsibleOfficer2.dropdown.value0";

    private static final String RESP_OFFICER2_TYPE_1 = "orders.responsibleOfficer2.dropdown.value1";

    private static final String RESP_OFFICER2_TYPE_2 = "orders.responsibleOfficer2.dropdown.value2";

    private static final String RESP_OFFICER2_TYPE_3 = "orders.responsibleOfficer2.dropdown.value3";

    private static final String RESP_OFFICER2_TYPE_4 = "orders.responsibleOfficer2.dropdown.value4";
    
    private static final String RESP_OFFICER2_TYPE_5 = "orders.responsibleOfficer2.dropdown.value5";

    private static final int TOTAL_RESP_OFFICER2 = 6;

    private static final String ORDER_PERIOD_1_TYPE_1 = "orders.period.value1";

    private static final String ORDER_PERIOD_1_TYPE_2 = "orders.period.value2";

    private static final String ORDER_PERIOD_1_TYPE_3 = "orders.period.value3";
    
    private static final String ORDER_PERIOD_1_TYPE_4 = "orders.period.value4";

    private static final int TOTAL_PERIOD_1 = 4;
    
    private static final String ORDER_PERIOD_2_TYPE_0 = "orders.period.value0";

    private static final String ORDER_PERIOD_2_TYPE_1 = "orders.period.value1";

    private static final String ORDER_PERIOD_2_TYPE_2 = "orders.period.value2";

    private static final String ORDER_PERIOD_2_TYPE_3 = "orders.period.value3";
    
    private static final String ORDER_PERIOD_2_TYPE_4 = "orders.period.value4";
    
    private static final String ORDER_PERIOD_2_TYPE_5 = "orders.period.value5";

    private static final int TOTAL_PERIOD_2 = 6;
    
    private static final String ORDER_DLA_TYPE_0 = "orders.dla.value0";

    private static final String ORDER_DLA_TYPE_1 = "orders.dla.value1";

    private static final String ORDER_DLA_TYPE_2 = "orders.dla.value2";

    private static final String ORDER_DLA_TYPE_3 = "orders.dla.value3";
    
    private static final String ORDER_DLA_TYPE_4 = "orders.dla.value4";

    private static final String ORDER_DLA_TYPE_5 = "orders.dla.value5";

    private static final String ORDER_DLA_TYPE_6 = "orders.dla.value6";

    private static final String ORDER_DLA_TYPE_7 = "orders.dla.value7";
    
    private static final String ORDER_DLA_TYPE_8 = "orders.dla.value8";

    private static final String ORDER_DLA_TYPE_9 = "orders.dla.value9";

    private static final String ORDER_DLA_TYPE_10 = "orders.dla.value10";

    private static final String ORDER_DLA_TYPE_11 = "orders.dla.value11";
    
    private static final String ORDER_DLA_TYPE_12 = "orders.dla.value12";

    private static final String ORDER_DLA_TYPE_13 = "orders.dla.value13";

    private static final String ORDER_DLA_TYPE_14 = "orders.dla.value14";

    private static final String ORDER_DLA_TYPE_15 = "orders.dla.value15";
    
    private static final String ORDER_DLA_TYPE_16 = "orders.dla.value16";

    private static final String ORDER_DLA_TYPE_17 = "orders.dla.value17";

    private static final String ORDER_DLA_TYPE_18 = "orders.dla.value18";

    private static final String ORDER_DLA_TYPE_19 = "orders.dla.value19";
    
    private static final String ORDER_DLA_TYPE_20 = "orders.dla.value20";

    private static final String ORDER_DLA_TYPE_21 = "orders.dla.value21";

    private static final String ORDER_DLA_TYPE_22 = "orders.dla.value22";

    private static final String ORDER_DLA_TYPE_23 = "orders.dla.value23";
    
    private static final String ORDER_DLA_TYPE_24 = "orders.dla.value24";

    private static final String ORDER_DLA_TYPE_25 = "orders.dla.value25";

    private static final String ORDER_DLA_TYPE_26 = "orders.dla.value26";

    private static final String ORDER_DLA_TYPE_27 = "orders.dla.value27";
    
    private static final String ORDER_DLA_TYPE_28 = "orders.dla.value28";

    private static final String ORDER_DLA_TYPE_29 = "orders.dla.value29";

    private static final String ORDER_DLA_TYPE_30 = "orders.dla.value30";

    private static final String ORDER_DLA_TYPE_31 = "orders.dla.value31";
    
    private static final String ORDER_DLA_TYPE_32 = "orders.dla.value32";

    private static final String ORDER_DLA_TYPE_33 = "orders.dla.value33";

    private static final String ORDER_DLA_TYPE_34 = "orders.dla.value34";

    private static final String ORDER_DLA_TYPE_35 = "orders.dla.value35";
    
    private static final String ORDER_DLA_TYPE_36 = "orders.dla.value36";

    private static final String ORDER_DLA_TYPE_37 = "orders.dla.value37";

    private static final String ORDER_DLA_TYPE_38 = "orders.dla.value38";

    private static final String ORDER_DLA_TYPE_39 = "orders.dla.value39";
    
    private static final String ORDER_DLA_TYPE_40 = "orders.dla.value40";

    private static final String ORDER_DLA_TYPE_41 = "orders.dla.value41";

    private static final String ORDER_DLA_TYPE_42 = "orders.dla.value42";

    private static final String ORDER_DLA_TYPE_43 = "orders.dla.value43";
    
    private static final String ORDER_DLA_TYPE_44 = "orders.dla.value44";

    private static final String ORDER_DLA_TYPE_45 = "orders.dla.value45";

    private static final String ORDER_DLA_TYPE_46 = "orders.dla.value46";

    private static final String ORDER_DLA_TYPE_47 = "orders.dla.value47";
    
    private static final String ORDER_DLA_TYPE_48 = "orders.dla.value48";

    private static final String ORDER_DLA_TYPE_49 = "orders.dla.value49";

    private static final String ORDER_DLA_TYPE_50 = "orders.dla.value50";

    private static final String ORDER_DLA_TYPE_51 = "orders.dla.value51";
    
    private static final String ORDER_DLA_TYPE_52 = "orders.dla.value52";

    private static final String ORDER_DLA_TYPE_53 = "orders.dla.value53";

    private static final String ORDER_DLA_TYPE_54 = "orders.dla.value54";

    private static final String ORDER_DLA_TYPE_55 = "orders.dla.value55";
    
    private static final String ORDER_DLA_TYPE_56 = "orders.dla.value56";

    private static final String ORDER_DLA_TYPE_57 = "orders.dla.value57";

    private static final String ORDER_DLA_TYPE_58 = "orders.dla.value58";

    private static final String ORDER_DLA_TYPE_59 = "orders.dla.value59";
    
    private static final String ORDER_DLA_TYPE_60 = "orders.dla.value60";

    private static final String ORDER_DLA_TYPE_61 = "orders.dla.value61";

    private static final String ORDER_DLA_TYPE_62 = "orders.dla.value62";

    private static final String ORDER_DLA_TYPE_63 = "orders.dla.value63";
    
    private static final String ORDER_DLA_TYPE_64 = "orders.dla.value64";

    private static final String ORDER_DLA_TYPE_65 = "orders.dla.value65";

    private static final String ORDER_DLA_TYPE_66 = "orders.dla.value66";

    private static final String ORDER_DLA_TYPE_67 = "orders.dla.value67";
    
    private static final String ORDER_DLA_TYPE_68 = "orders.dla.value68";

    private static final String ORDER_DLA_TYPE_69 = "orders.dla.value69";

    private static final String ORDER_DLA_TYPE_70 = "orders.dla.value70";

    private static final String ORDER_DLA_TYPE_71 = "orders.dla.value71";
    
    private static final String ORDER_DLA_TYPE_72 = "orders.dla.value72";
    
    private static final String ORDER_DLA_TYPE_73 = "orders.dla.value73";

    private static final String ORDER_DLA_TYPE_74 = "orders.dla.value74";

    private static final String ORDER_DLA_TYPE_75 = "orders.dla.value75";

    private static final String ORDER_DLA_TYPE_76 = "orders.dla.value76";
    
    private static final String ORDER_DLA_TYPE_77 = "orders.dla.value77";

    private static final String ORDER_DLA_TYPE_78 = "orders.dla.value78";

    private static final String ORDER_DLA_TYPE_79 = "orders.dla.value79";

    private static final String ORDER_DLA_TYPE_80 = "orders.dla.value80";
    
    private static final String ORDER_DLA_TYPE_81 = "orders.dla.value81";

    private static final String ORDER_DLA_TYPE_82 = "orders.dla.value82";

    private static final String ORDER_DLA_TYPE_83 = "orders.dla.value83";

    private static final String ORDER_DLA_TYPE_84 = "orders.dla.value84";
    
    private static final String ORDER_DLA_TYPE_85 = "orders.dla.value85";

    private static final String ORDER_DLA_TYPE_86 = "orders.dla.value86";

    private static final String ORDER_DLA_TYPE_87 = "orders.dla.value87";

    private static final String ORDER_DLA_TYPE_88 = "orders.dla.value88";
    
    private static final String ORDER_DLA_TYPE_89 = "orders.dla.value89";

    private static final String ORDER_DLA_TYPE_90 = "orders.dla.value90";

    private static final String ORDER_DLA_TYPE_91 = "orders.dla.value91";

    private static final String ORDER_DLA_TYPE_92 = "orders.dla.value92";
    
    private static final String ORDER_DLA_TYPE_93 = "orders.dla.value93";

    private static final String ORDER_DLA_TYPE_94 = "orders.dla.value94";

    private static final String ORDER_DLA_TYPE_95 = "orders.dla.value95";

    private static final String ORDER_DLA_TYPE_96 = "orders.dla.value96";
    
    private static final String ORDER_DLA_TYPE_97 = "orders.dla.value97";

    private static final String ORDER_DLA_TYPE_98 = "orders.dla.value98";

    private static final String ORDER_DLA_TYPE_99 = "orders.dla.value99";
    
    private static final String ORDER_DLA_TYPE_100 = "orders.dla.value100";

    private static final String ORDER_DLA_TYPE_101 = "orders.dla.value101";

    private static final String ORDER_DLA_TYPE_102 = "orders.dla.value102";

    private static final String ORDER_DLA_TYPE_103 = "orders.dla.value103";
    
    private static final String ORDER_DLA_TYPE_104 = "orders.dla.value104";

    private static final String ORDER_DLA_TYPE_105 = "orders.dla.value105";

    private static final String ORDER_DLA_TYPE_106 = "orders.dla.value106";

    private static final String ORDER_DLA_TYPE_107 = "orders.dla.value107";
    
    private static final String ORDER_DLA_TYPE_108 = "orders.dla.value108";

    private static final String ORDER_DLA_TYPE_109 = "orders.dla.value109";

    private static final String ORDER_DLA_TYPE_110 = "orders.dla.value110";

    private static final String ORDER_DLA_TYPE_111 = "orders.dla.value111";
    
    private static final String ORDER_DLA_TYPE_112 = "orders.dla.value112";

    private static final String ORDER_DLA_TYPE_113 = "orders.dla.value113";

    private static final String ORDER_DLA_TYPE_114 = "orders.dla.value114";

    private static final String ORDER_DLA_TYPE_115 = "orders.dla.value115";
    
    private static final String ORDER_DLA_TYPE_116 = "orders.dla.value116";

    private static final String ORDER_DLA_TYPE_117 = "orders.dla.value117";

    private static final String ORDER_DLA_TYPE_118 = "orders.dla.value118";

    private static final String ORDER_DLA_TYPE_119 = "orders.dla.value119";
    
    private static final String ORDER_DLA_TYPE_120 = "orders.dla.value120";

    private static final String ORDER_DLA_TYPE_121 = "orders.dla.value121";

    private static final String ORDER_DLA_TYPE_122 = "orders.dla.value122";

    private static final String ORDER_DLA_TYPE_123 = "orders.dla.value123";
    
    private static final String ORDER_DLA_TYPE_124 = "orders.dla.value124";

    private static final String ORDER_DLA_TYPE_125 = "orders.dla.value125";

    private static final String ORDER_DLA_TYPE_126 = "orders.dla.value126";

    private static final String ORDER_DLA_TYPE_127 = "orders.dla.value127";
    
    private static final String ORDER_DLA_TYPE_128 = "orders.dla.value128";

    private static final String ORDER_DLA_TYPE_129 = "orders.dla.value129";

    private static final String ORDER_DLA_TYPE_130 = "orders.dla.value130";

    private static final String ORDER_DLA_TYPE_131 = "orders.dla.value131";
    
    private static final String ORDER_DLA_TYPE_132 = "orders.dla.value132";

    private static final String ORDER_DLA_TYPE_133 = "orders.dla.value133";

    private static final String ORDER_DLA_TYPE_134 = "orders.dla.value134";

    private static final String ORDER_DLA_TYPE_135 = "orders.dla.value135";
    
    private static final String ORDER_DLA_TYPE_136 = "orders.dla.value136";

    private static final String ORDER_DLA_TYPE_137 = "orders.dla.value137";

    private static final String ORDER_DLA_TYPE_138 = "orders.dla.value138";

    private static final String ORDER_DLA_TYPE_139 = "orders.dla.value139";
    
    private static final String ORDER_DLA_TYPE_140 = "orders.dla.value140";

    private static final String ORDER_DLA_TYPE_141 = "orders.dla.value141";

    private static final String ORDER_DLA_TYPE_142 = "orders.dla.value142";

    private static final String ORDER_DLA_TYPE_143 = "orders.dla.value143";
    
    private static final String ORDER_DLA_TYPE_144 = "orders.dla.value144";

    private static final String ORDER_DLA_TYPE_145 = "orders.dla.value145";

    private static final String ORDER_DLA_TYPE_146 = "orders.dla.value146";

    private static final String ORDER_DLA_TYPE_147 = "orders.dla.value147";
    
    private static final String ORDER_DLA_TYPE_148 = "orders.dla.value148";

    private static final String ORDER_DLA_TYPE_149 = "orders.dla.value149";

    private static final String ORDER_DLA_TYPE_150 = "orders.dla.value150";

    private static final String ORDER_DLA_TYPE_151 = "orders.dla.value151";
    
    private static final String ORDER_DLA_TYPE_152 = "orders.dla.value152";

    private static final String ORDER_DLA_TYPE_153 = "orders.dla.value153";

    private static final String ORDER_DLA_TYPE_154 = "orders.dla.value154";

    private static final String ORDER_DLA_TYPE_155 = "orders.dla.value155";
    
    private static final String ORDER_DLA_TYPE_156 = "orders.dla.value156";

    private static final String ORDER_DLA_TYPE_157 = "orders.dla.value157";

    private static final String ORDER_DLA_TYPE_158 = "orders.dla.value158";

    private static final String ORDER_DLA_TYPE_159 = "orders.dla.value159";
    
    private static final String ORDER_DLA_TYPE_160 = "orders.dla.value160";
    
    private static final String ORDER_DLA_TYPE_161 = "orders.dla.value161";

    private static final String ORDER_DLA_TYPE_162 = "orders.dla.value162";

    private static final String ORDER_DLA_TYPE_163 = "orders.dla.value163";
    
    private static final String ORDER_DLA_TYPE_164 = "orders.dla.value164";

    private static final String ORDER_DLA_TYPE_165 = "orders.dla.value165";
    
    private static final int TOTAL_DLA = 166;
    
    private static final String ORDER_D20_DROP1_0 = "orders.d20.dropdown1.value0";
    
    private static final String ORDER_D20_DROP1_1 = "orders.d20.dropdown1.value1";
    
    private static final String ORDER_D20_DROP1_2 = "orders.d20.dropdown1.value2";
    
    private static final int TOTAL_D20_DROPDOWN1 = 3;
    
    private static final String ORDER_D20_DROP2_0 = "orders.d20.dropdown2.value0";
    
    private static final String ORDER_D20_DROP2_1 = "orders.d20.dropdown2.value1";
    
    private static final String ORDER_D20_DROP2_2 = "orders.d20.dropdown2.value2";
    
    private static final int TOTAL_D20_DROPDOWN2 = 3;
    
    private static final String ORDER_D20_DROP0_0 = "orders.d20.dropdown0.value0";
    
    private static final String ORDER_D20_DROP0_1 = "orders.d20.dropdown0.value1";
    
    private static final String ORDER_D20_DROP0_2 = "orders.d20.dropdown0.value2";
    
    private static final String ORDER_D20_DROP0_3 = "orders.d20.dropdown0.value3";
    
    private static final String ORDER_D20_DROP0_4 = "orders.d20.dropdown0.value4";
    
    private static final int TOTAL_D20_DROPDOWN0 = 5;
    
    private static final String ORDER_D20_DROP3_0 = "orders.d20.dropdown3.value0";
    
    private static final String ORDER_D20_DROP3_1 = "orders.d20.dropdown3.value1";
    
    private static final String ORDER_D20_DROP3_2 = "orders.d20.dropdown3.value2";
    
    private static final String ORDER_D20_DROP3_3 = "orders.d20.dropdown3.value3";
    
    private static final String ORDER_D20_DROP3_4 = "orders.d20.dropdown3.value4";
    
    private static final String ORDER_D20_DROP3_5 = "orders.d20.dropdown3.value5";
    
    private static final String ORDER_D20_DROP3_6 = "orders.d20.dropdown3.value6";
    
    private static final String ORDER_D20_DROP3_7 = "orders.d20.dropdown3.value7";
    
    private static final String ORDER_D20_DROP3_8 = "orders.d20.dropdown3.value8";
    
    private static final String ORDER_D20_DROP3_9 = "orders.d20.dropdown3.value9";
    
    private static final String ORDER_D20_DROP3_10 = "orders.d20.dropdown3.value10";
    
    private static final String ORDER_D20_DROP3_11 = "orders.d20.dropdown3.value11";
    
    private static final String ORDER_D20_DROP3_12 = "orders.d20.dropdown3.value12";
    
    private static final String ORDER_D20_DROP3_13 = "orders.d20.dropdown3.value13";
    
    private static final String ORDER_D20_DROP3_14 = "orders.d20.dropdown3.value14";
    
    private static final String ORDER_D20_DROP3_15 = "orders.d20.dropdown3.value15";
    
    private static final String ORDER_D20_DROP3_16 = "orders.d20.dropdown3.value16";
    
    private static final String ORDER_D20_DROP3_17 = "orders.d20.dropdown3.value17";
    
    private static final String ORDER_D20_DROP3_18 = "orders.d20.dropdown3.value18";
    
    private static final String ORDER_D20_DROP3_19 = "orders.d20.dropdown3.value19";
    
    private static final String ORDER_D20_DROP3_20 = "orders.d20.dropdown3.value20";
    
    private static final int TOTAL_D20_DROPDOWN3 = 21;
    
    private static final String ORDER_D20_INTERIMFINAL_0 = "orders.d20.interimfinal.value0";
    
    private static final String ORDER_D20_INTERIMFINAL_1 = "orders.d20.interimfinal.value1";
    
    private static final String ORDER_D20_INTERIMFINAL_2 = "orders.d20.interimfinal.value2";
    
    private static final int TOTAL_D20_INTERIMFINAL = 3;
    
    private static final String ORDER_D20_OTHER_SENTENCE_0 = "orders.d20.othersentence.value0";
    
    private static final String ORDER_D20_OTHER_SENTENCE_1 = "orders.d20.othersentence.value1";
    
    private static final String ORDER_D20_OTHER_SENTENCE_2 = "orders.d20.othersentence.value2";
    
    private static final String ORDER_D20_OTHER_SENTENCE_3 = "orders.d20.othersentence.value3";
    
    private static final String ORDER_D20_OTHER_SENTENCE_4 = "orders.d20.othersentence.value4";
    
    private static final String ORDER_D20_OTHER_SENTENCE_5 = "orders.d20.othersentence.value5";
    
    private static final String ORDER_D20_OTHER_SENTENCE_6 = "orders.d20.othersentence.value6";
    
    private static final String ORDER_D20_OTHER_SENTENCE_7 = "orders.d20.othersentence.value7";
    
    private static final String ORDER_D20_OTHER_SENTENCE_8 = "orders.d20.othersentence.value8";
    
    private static final String ORDER_D20_OTHER_SENTENCE_9 = "orders.d20.othersentence.value9";
    
    private static final String ORDER_D20_OTHER_SENTENCE_10 = "orders.d20.othersentence.value10";
    
    private static final String ORDER_D20_OTHER_SENTENCE_11 = "orders.d20.othersentence.value11";
    
    private static final String ORDER_D20_OTHER_SENTENCE_12 = "orders.d20.othersentence.value12";
    
    private static final String ORDER_D20_OTHER_SENTENCE_13 = "orders.d20.othersentence.value13";
    
    private static final String ORDER_D20_OTHER_SENTENCE_14 = "orders.d20.othersentence.value14";
    
    private static final int TOTAL_D20_OTHERSENTENCE = 15;
    
    private static final String ORDER_D20_PPSCO_0 = "orders.d20.ppsco.value0";
    
    private static final String ORDER_D20_PPSCO_1 = "orders.d20.ppsco.value1";
    
    private static final int TOTAL_D20_PPSCO = 2;
    
    private static final String ORDER_SALUTATION_0 = "orders.salutation.value0";
    
    private static final String ORDER_SALUTATION_1 = "orders.salutation.value1";
    
    private static final String ORDER_SALUTATION_2 = "orders.salutation.value2";
    
    private static final String ORDER_SALUTATION_3 = "orders.salutation.value3";
    
    private static final String ORDER_SALUTATION_4 = "orders.salutation.value4";
        
    private static final int TOTAL_SALUTATION = 5;
    
    private static final String ORDER_GENDER_0 = "orders.gender.value0";
    
    private static final String ORDER_GENDER_1 = "orders.gender.value1";
        
    private static final int TOTAL_GENDER = 2;
    
    private Dimension dropDownSize = new Dimension();
    
    private CustomXhibitComboBox box;

    public void initComponent() {
        String type = getHelper().getAttribute("type");
        // drop down list containing Order Types
        if (type.equals("one")) {
            String[] list = new String[TOTAL_ORDERS];
            list[0] = ResourceHelper.getResourceString(ORDER_TYPE_0);
            list[1] = ResourceHelper.getResourceString(ORDER_TYPE_1);
            list[2] = ResourceHelper.getResourceString(ORDER_TYPE_2);
            list[3] = ResourceHelper.getResourceString(ORDER_TYPE_3);
            list[4] = ResourceHelper.getResourceString(ORDER_TYPE_4);
            list[5] = ResourceHelper.getResourceString(ORDER_TYPE_5);
            list[6] = ResourceHelper.getResourceString(ORDER_TYPE_6);
            list[7] = ResourceHelper.getResourceString(ORDER_TYPE_7);
            list[8] = ResourceHelper.getResourceString(ORDER_TYPE_8);
            this.dropDownSize.setSize(85, 22);
            box = new CustomXhibitComboBox(list, dropDownSize);
            box.setEditable(false);
            setUp(box);
        }
        // drop down list containing programme options
        else if (type.equals("two")) {
            String[] list = new String[TOTAL_PROGRAMMES];
            list[0] = ResourceHelper.getResourceString(PROG_TYPE_0);
            list[1] = ResourceHelper.getResourceString(PROG_TYPE_1);
            list[2] = ResourceHelper.getResourceString(PROG_TYPE_2);
            list[3] = ResourceHelper.getResourceString(PROG_TYPE_3);
            list[4] = ResourceHelper.getResourceString(PROG_TYPE_4);
            list[5] = ResourceHelper.getResourceString(PROG_TYPE_5);
            this.dropDownSize.setSize(260, 22); // need to adjust
            box = new CustomXhibitComboBox(list, dropDownSize);
            box.setEditable(false);
            setUp(box);
        }
        // drop down list containing responsible officers 1 
        else if (type.equals("three")) {
            String[] list = new String[TOTAL_RESP_OFFICER1];
            list[0] = ResourceHelper.getResourceString(RESP_OFFICER1_TYPE_0);
            list[1] = ResourceHelper.getResourceString(RESP_OFFICER1_TYPE_1);
            list[2] = ResourceHelper.getResourceString(RESP_OFFICER1_TYPE_2);
            list[3] = ResourceHelper.getResourceString(RESP_OFFICER1_TYPE_3);
            list[4] = ResourceHelper.getResourceString(RESP_OFFICER1_TYPE_4);
            list[5] = ResourceHelper.getResourceString(RESP_OFFICER1_TYPE_5);
            this.dropDownSize.setSize(230, 22);
            box = new CustomXhibitComboBox(list, dropDownSize);
            box.setEditable(false);
            setUp(box);
        }

        // drop down list containing responsible officers 2
        else if (type.equals("four")) {
            String[] list = new String[TOTAL_RESP_OFFICER2];
            list[0] = ResourceHelper.getResourceString(RESP_OFFICER2_TYPE_0);
            list[1] = ResourceHelper.getResourceString(RESP_OFFICER2_TYPE_1);
            list[2] = ResourceHelper.getResourceString(RESP_OFFICER2_TYPE_2);
            list[3] = ResourceHelper.getResourceString(RESP_OFFICER2_TYPE_3);
            list[4] = ResourceHelper.getResourceString(RESP_OFFICER2_TYPE_4);
            list[5] = ResourceHelper.getResourceString(RESP_OFFICER2_TYPE_5);
            this.dropDownSize.setSize(230, 22);
            box = new CustomXhibitComboBox(list, dropDownSize);
            box.setEditable(false);
            setUp(box);
        }

        // drop down list containing order periods (days, weeks, months, years)
        else if (type.equals("five")) {
            String[] list = new String[TOTAL_PERIOD_1];
            list[0] = ResourceHelper.getResourceString(ORDER_PERIOD_1_TYPE_1);
            list[1] = ResourceHelper.getResourceString(ORDER_PERIOD_1_TYPE_2);
            list[2] = ResourceHelper.getResourceString(ORDER_PERIOD_1_TYPE_3);
            list[3] = ResourceHelper.getResourceString(ORDER_PERIOD_1_TYPE_4);
            this.dropDownSize.setSize(70, 22);
            box = new CustomXhibitComboBox(list, dropDownSize);
            box.setEditable(false);
            setUp(box);
        }
        
        // drop down list containing order periods (hours, days, weeks, months, years)
        else if (type.equals("five_1")) {
            String[] list = new String[TOTAL_PERIOD_2];
            list[0] = ResourceHelper.getResourceString(ORDER_PERIOD_2_TYPE_0);
            list[1] = ResourceHelper.getResourceString(ORDER_PERIOD_2_TYPE_1);
            list[2] = ResourceHelper.getResourceString(ORDER_PERIOD_2_TYPE_2);
            list[3] = ResourceHelper.getResourceString(ORDER_PERIOD_2_TYPE_3);
            list[4] = ResourceHelper.getResourceString(ORDER_PERIOD_2_TYPE_4);
            list[5] = ResourceHelper.getResourceString(ORDER_PERIOD_2_TYPE_5);
            this.dropDownSize.setSize(130, 22);
            box = new CustomXhibitComboBox(list, dropDownSize);
            box.setEditable(false);
            setUp(box);
        }
        
        // drop down list containing Designated Local Authority
        else if (type.equals("six")) {
            String[] list = new String[TOTAL_DLA];
            list[0] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_0);
            list[1] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_1);
            list[2] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_2);
            list[3] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_3);
            list[4] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_4);
            list[5] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_5);
            list[6] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_6);
            list[7] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_7);
            list[8] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_8);
            list[9] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_9);
            list[10] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_10);
            list[11] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_11);
            list[12] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_12);
            list[13] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_13);
            list[14] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_14);
            list[15] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_15);
            list[16] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_16);
            list[17] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_17);
            list[18] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_18);
            list[19] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_19);
            list[20] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_20);
            list[21] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_21);
            list[22] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_22);
            list[23] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_23);
            list[24] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_24);
            list[25] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_25);
            list[26] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_26);
            list[27] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_27);
            list[28] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_28);
            list[29] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_29);
            list[30] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_30);
            list[31] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_31);
            list[32] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_32);
            list[33] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_33);
            list[34] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_34);
            list[35] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_35);
            list[36] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_36);
            list[37] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_37);
            list[38] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_38);
            list[39] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_39);
            list[40] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_40);
            list[41] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_41);
            list[42] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_42);
            list[43] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_43);
            list[44] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_44);
            list[45] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_45);
            list[46] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_46);
            list[47] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_47);
            list[48] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_48);
            list[49] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_49);
            list[50] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_50);
            list[51] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_51);
            list[52] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_52);
            list[53] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_53);
            list[54] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_54);
            list[55] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_55);
            list[56] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_56);
            list[57] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_57);
            list[58] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_58);
            list[59] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_59);
            list[60] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_60);
            list[61] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_61);
            list[62] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_62);
            list[63] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_63);
            list[64] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_64);
            list[65] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_65);
            list[66] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_66);
            list[67] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_67);
            list[68] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_68);
            list[69] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_69);
            list[70] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_70);
            list[71] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_71);
            list[72] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_72);
            list[73] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_73);
            list[74] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_74);
            list[75] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_75);
            list[76] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_76);
            list[77] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_77);
            list[78] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_78);
            list[79] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_79);
            list[80] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_80);
            list[81] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_81);
            list[82] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_82);
            list[83] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_83);
            list[84] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_84);
            list[85] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_85);
            list[86] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_86);
            list[87] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_87);
            list[88] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_88);
            list[89] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_89);
            list[90] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_90);
            list[91] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_91);
            list[92] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_92);
            list[93] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_93);
            list[94] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_94);
            list[95] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_95);
            list[96] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_96);
            list[97] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_97);
            list[98] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_98);
            list[99] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_99);
            list[100] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_100);
            list[101] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_101);
            list[102] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_102);
            list[103] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_103);
            list[104] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_104);
            list[105] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_105);
            list[106] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_106);
            list[107] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_107);
            list[108] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_108);
            list[109] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_109);
            list[110] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_110);
            list[111] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_111);
            list[112] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_112);
            list[113] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_113);
            list[114] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_114);
            list[115] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_115);
            list[116] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_116);
            list[117] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_117);
            list[118] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_118);
            list[119] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_119);
            list[120] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_120);
            list[121] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_121);
            list[122] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_122);
            list[123] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_123);
            list[124] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_124);
            list[125] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_125);
            list[126] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_126);
            list[127] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_127);
            list[128] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_128);
            list[129] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_129);
            list[130] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_130);
            list[131] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_131);
            list[132] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_132);
            list[133] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_133);
            list[134] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_134);
            list[135] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_135);
            list[136] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_136);
            list[137] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_137);
            list[138] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_138);
            list[139] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_139);
            list[140] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_140);
            list[141] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_141);
            list[142] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_142);
            list[143] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_143);
            list[144] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_144);
            list[145] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_145);
            list[146] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_146);
            list[147] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_147);
            list[148] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_148);
            list[149] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_149);
            list[150] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_150);
            list[151] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_151);
            list[152] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_152);
            list[153] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_153);
            list[154] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_154);
            list[155] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_155);
            list[156] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_156);
            list[157] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_157);
            list[158] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_158);
            list[159] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_159);
            list[160] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_160);
            list[161] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_161);
            list[162] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_162);
            list[163] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_163);
            list[164] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_164);
            list[165] = ResourceHelper.getResourceString(ORDER_DLA_TYPE_165);
            this.dropDownSize.setSize(220, 22);
            box = new CustomXhibitComboBox(list, dropDownSize);
            box.setEditable(false);
            setUp(box);
        }
        
        // drop down list containing D20 dropdown elements
        else if (type.equals("seven")) {
            String[] list = new String[TOTAL_D20_DROPDOWN1];
            list[0] = ResourceHelper.getResourceString(ORDER_D20_DROP1_0);
            list[1] = ResourceHelper.getResourceString(ORDER_D20_DROP1_1);
            list[2] = ResourceHelper.getResourceString(ORDER_D20_DROP1_2);
            this.dropDownSize.setSize(120, 22);
            box = new CustomXhibitComboBox(list, dropDownSize);
            box.setEditable(false);
            setUp(box);
        }
        
        // drop down list containing D20 dropdown elements
        else if (type.equals("eight")) {
            String[] list = new String[TOTAL_D20_DROPDOWN2];
            list[0] = ResourceHelper.getResourceString(ORDER_D20_DROP2_0);
            list[1] = ResourceHelper.getResourceString(ORDER_D20_DROP2_1);
            list[2] = ResourceHelper.getResourceString(ORDER_D20_DROP2_2);
            this.dropDownSize.setSize(120, 22);
            box = new CustomXhibitComboBox(list, dropDownSize);
            box.setEditable(false);
            setUp(box);
        }
        
        // drop down list containing D20 dropdown elements
        else if (type.equals("nine")) {
            String[] list = new String[TOTAL_D20_DROPDOWN0];
            list[0] = ResourceHelper.getResourceString(ORDER_D20_DROP0_0);
            list[1] = ResourceHelper.getResourceString(ORDER_D20_DROP0_1);
            list[2] = ResourceHelper.getResourceString(ORDER_D20_DROP0_2);
            list[3] = ResourceHelper.getResourceString(ORDER_D20_DROP0_3);
            list[4] = ResourceHelper.getResourceString(ORDER_D20_DROP0_4);
            this.dropDownSize.setSize(280, 22);
            box = new CustomXhibitComboBox(list, dropDownSize);
            box.setEditable(false);
            setUp(box);
        }
        
        // drop down list containing D20 dropdown elements
        else if (type.equals("ten")) {
            String[] list = new String[TOTAL_D20_DROPDOWN3];
            list[0] = ResourceHelper.getResourceString(ORDER_D20_DROP3_0);
            list[1] = ResourceHelper.getResourceString(ORDER_D20_DROP3_1);
            list[2] = ResourceHelper.getResourceString(ORDER_D20_DROP3_2);
            list[3] = ResourceHelper.getResourceString(ORDER_D20_DROP3_3);
            list[4] = ResourceHelper.getResourceString(ORDER_D20_DROP3_4);
            list[5] = ResourceHelper.getResourceString(ORDER_D20_DROP3_5);
            list[6] = ResourceHelper.getResourceString(ORDER_D20_DROP3_6);
            list[7] = ResourceHelper.getResourceString(ORDER_D20_DROP3_7);
            list[8] = ResourceHelper.getResourceString(ORDER_D20_DROP3_8);
            list[9] = ResourceHelper.getResourceString(ORDER_D20_DROP3_9);
            list[10] = ResourceHelper.getResourceString(ORDER_D20_DROP3_10);
            list[11] = ResourceHelper.getResourceString(ORDER_D20_DROP3_11);
            list[12] = ResourceHelper.getResourceString(ORDER_D20_DROP3_12);
            list[13] = ResourceHelper.getResourceString(ORDER_D20_DROP3_13);
            list[14] = ResourceHelper.getResourceString(ORDER_D20_DROP3_14);
            list[15] = ResourceHelper.getResourceString(ORDER_D20_DROP3_15);
            list[16] = ResourceHelper.getResourceString(ORDER_D20_DROP3_16);
            list[17] = ResourceHelper.getResourceString(ORDER_D20_DROP3_17);
            list[18] = ResourceHelper.getResourceString(ORDER_D20_DROP3_18);
            list[19] = ResourceHelper.getResourceString(ORDER_D20_DROP3_19);
            list[20] = ResourceHelper.getResourceString(ORDER_D20_DROP3_20);
            this.dropDownSize.setSize(190, 22);
            box = new CustomXhibitComboBox(list, dropDownSize);
            box.setEditable(false);
            setUp(box);
        }
        
        // drop down list containing D20 other sentence dropdown elements
        else if (type.equals("eleven")) {
            String[] list = new String[TOTAL_D20_OTHERSENTENCE];
            list[0] = ResourceHelper.getResourceString(ORDER_D20_OTHER_SENTENCE_0);
            list[1] = ResourceHelper.getResourceString(ORDER_D20_OTHER_SENTENCE_1);
            list[2] = ResourceHelper.getResourceString(ORDER_D20_OTHER_SENTENCE_2);
            list[3] = ResourceHelper.getResourceString(ORDER_D20_OTHER_SENTENCE_3);
            list[4] = ResourceHelper.getResourceString(ORDER_D20_OTHER_SENTENCE_4);
            list[5] = ResourceHelper.getResourceString(ORDER_D20_OTHER_SENTENCE_5);
            list[6] = ResourceHelper.getResourceString(ORDER_D20_OTHER_SENTENCE_6);
            list[7] = ResourceHelper.getResourceString(ORDER_D20_OTHER_SENTENCE_7);
            list[8] = ResourceHelper.getResourceString(ORDER_D20_OTHER_SENTENCE_8);
            list[9] = ResourceHelper.getResourceString(ORDER_D20_OTHER_SENTENCE_9);
            list[10] = ResourceHelper.getResourceString(ORDER_D20_OTHER_SENTENCE_10);
            list[11] = ResourceHelper.getResourceString(ORDER_D20_OTHER_SENTENCE_11);
            list[12] = ResourceHelper.getResourceString(ORDER_D20_OTHER_SENTENCE_12);
            list[13] = ResourceHelper.getResourceString(ORDER_D20_OTHER_SENTENCE_13);
            list[14] = ResourceHelper.getResourceString(ORDER_D20_OTHER_SENTENCE_14);
            this.dropDownSize.setSize(350, 22);
            box = new CustomXhibitComboBox(list, dropDownSize);
            box.setEditable(false);
            setUp(box);
        }
        
        // drop down list containing D20 dropdown elements
        else if (type.equals("twelve")) {
            String[] list = new String[TOTAL_D20_PPSCO];
            list[0] = ResourceHelper.getResourceString(ORDER_D20_PPSCO_0);
            list[1] = ResourceHelper.getResourceString(ORDER_D20_PPSCO_1);
            this.dropDownSize.setSize(300, 22);
            box = new CustomXhibitComboBox(list, dropDownSize);
            box.setEditable(false);
            setUp(box);
        }
        
        // drop down list containing salutation dropdown elements
        else if (type.equals("thirteen")) {
            String[] list = new String[TOTAL_SALUTATION];
            list[0] = ResourceHelper.getResourceString(ORDER_SALUTATION_0);
            list[1] = ResourceHelper.getResourceString(ORDER_SALUTATION_1);
            list[2] = ResourceHelper.getResourceString(ORDER_SALUTATION_2);
            list[3] = ResourceHelper.getResourceString(ORDER_SALUTATION_3);
            list[4] = ResourceHelper.getResourceString(ORDER_SALUTATION_4);
            this.dropDownSize.setSize(120, 22);
            box = new CustomXhibitComboBox(list, dropDownSize);
            box.setEditable(false);
            setUp(box);
        }
        
        // drop down list containing gender dropdown elements
        else if (type.equals("fourteen")) {
            String[] list = new String[TOTAL_GENDER];
            list[0] = ResourceHelper.getResourceString(ORDER_GENDER_0);
            list[1] = ResourceHelper.getResourceString(ORDER_GENDER_1);
            this.dropDownSize.setSize(150, 22);
            box = new CustomXhibitComboBox(list, dropDownSize);
            box.setEditable(false);
            setUp(box);
        }
        
        // drop down list containing gender dropdown elements
        else if (type.equals("fifteen")) {
            String[] list = new String[TOTAL_D20_INTERIMFINAL];
            list[0] = ResourceHelper.getResourceString(ORDER_D20_INTERIMFINAL_0);
            list[1] = ResourceHelper.getResourceString(ORDER_D20_INTERIMFINAL_1);
            list[2] = ResourceHelper.getResourceString(ORDER_D20_INTERIMFINAL_2);
            for (int i=0;i < list.length; i++) {
            	list[i] = list[i].length() == 0 ? " " : list[i];
            }
            this.dropDownSize.setSize(150, 22);
            box = new CustomXhibitComboBox(list, dropDownSize);
            box.setEditable(false);
            setUp(box);
        }
        
    }

    public void itemStateChanged(ItemEvent e) {
    	if (isInitialised() && isReadOnly()) {
    		// Only ask on the SELECTED event rather than the DESELECTED event
    		if (ItemEvent.SELECTED == e.getStateChange()) {
    			if (showConfirmOverrideMsg((Frame) this.getParent())) {
    				setValue();
    			} else {
    				revertValue();
    			}
    		}
    	} else {
    		setValue();
    	}
    }
    
    private void setValue() {
    	getHelper().setValue(((JComboBox) getVisualComponent()).getSelectedItem().toString());
    }
    
    private void revertValue() {
    	boolean isInitialised = isInitialised();
    	setInitialised(false);
    	box.setSelectedItem(getHelper().getValue());
    	setInitialised(isInitialised);
    }

    public void setEnabled(boolean enabled) {
    	box.setEnabled(enabled);
    	super.setEnabled(enabled);
    }
    
    private void setUp(CustomXhibitComboBox box) {
        setVisualComponent(box);
        box.addItemListener(this);
        String text = getHelper().getValue();
        
        if(getHelper().getOrderDataReference().equals("//ord:D20/ord:LicenceType")&& text!=null)
        {
        	String tmpText = text.trim(); 
        	text = text.isEmpty()? "0": tmpText.charAt(0)+"";
        	for (int index=0; index < box.getList().length; index++ ) {
        		if (box.getList()[index].startsWith(text)) {
        			box.setSelectedIndex(index);
        		}
        	}
        }
        else if(isD20Order())
        {
        	boolean valueInList = isValueInList(text, box.getList());
	        if ((text != null) && (text.length() > 0) && valueInList) {
	            box.setSelectedItem(text);
	        }
        }
        else {
	        boolean valueInList = isValueInList(text, box.getList());
	        if ((text != null) && (text.length() > 0) && valueInList) {
	            box.setSelectedItem(text);
	            box.setEditable(false);
	        }
        }
    }
    
    
    /**
     * If dropdown entries change in orders then you wont be able to save a copy of an old one unless
     * it defaults to a valid item.
     * @param item
     * @param items
     * @return
     */
    private boolean isValueInList(String item, String[] items) {
    	boolean valueInList = false;
    	
    	for (int i=0; i<items.length; i++) {
    		if (item.equals(items[i])) {
    			valueInList = true;
    			break;
    		}
    	}
    	
    	return valueInList;
    }
}
