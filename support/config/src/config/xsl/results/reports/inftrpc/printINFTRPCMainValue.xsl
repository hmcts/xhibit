<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
<xsl:template name="INFTRPCMainReport"> 
<xsl:for-each select="./inftrpc-main-values">
			
		<fo:table>
			<fo:table-column column-width="160mm"/>
			<fo:table-column column-width="40mm"/>
			
			<fo:table-body>
				<fo:table-row>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="bold" text-align="left">
								<xsl:text> P.  TOTAL NUMBER OF TRIALS LISTED IN MONTH:</xsl:text>
							</fo:block>		
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="bold" text-align="left">
								<xsl:value-of select="./pnumber-listed-trials"/> 
							</fo:block>	
						</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								<xsl:text> Q. NUMBER OF EFFECTIVE TRIALS IN MONTH:</xsl:text>
							</fo:block>		
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								<xsl:value-of select="./qnumber-effective-trials"/>  <xsl:text> (</xsl:text> <xsl:value-of select="./qnumber-effective-trials-perc"/>  <xsl:text>% of total )</xsl:text>
							</fo:block>	
						</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								<xsl:text> R. NUMBER OF CRACKED TRIALS IN MONTH:</xsl:text>
							</fo:block>		
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								<xsl:value-of select="./rnumber-cracked-trials"/>  <xsl:text> (</xsl:text>  <xsl:value-of select="./rnumber-cracked-trials-perc"/> <xsl:text>% of total )</xsl:text>
							</fo:block>	
						</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								<xsl:text> S. NUMBER OF INEFFECTIVE LISTINGS IN  MONTH:</xsl:text>
							</fo:block>		
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								<xsl:value-of select="./snumber-ineffective-listings"/>  <xsl:text> (</xsl:text>  <xsl:value-of select="./snumber-ineffective-perc"/> <xsl:text>% of total )</xsl:text>
							</fo:block>	
						</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								<xsl:text>T. COMMITTALS/TRANSFERS FOR TRIAL DISPOSED OF IN MONTH:</xsl:text>
							</fo:block>		
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								<xsl:value-of select="./tnumber-disposed-trials"/>
							</fo:block>	
						</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								<xsl:text> U. BROAD CRACKED TRIAL RATE(R/T x 100)</xsl:text>
							</fo:block>		
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								<xsl:value-of select="./broad-cracked-trial"/>
							</fo:block>	
						</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
						<fo:table-cell display-align="after">
							<fo:block margin-top="3mm" font-size="11pt" font-family="sans-serif" font-weight="bold" text-align="left">
								<xsl:text>REASONS FOR CRACKED TRIALS</xsl:text>
							</fo:block>		
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="11pt" font-family="sans-serif" font-weight="bold" text-align="left">
								 <xsl:text>NUMBER AND PERCENTAGE IN MONTH</xsl:text>
							</fo:block>	
						</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								 <xsl:text>A Acceptable guilty plea(s) entered late to some or all charge / counts on the charge sheet, offered for first time by the defence</xsl:text> 
							</fo:block>		
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								<xsl:value-of select="./areasons-cracked-trials"/><xsl:text> (</xsl:text> <xsl:value-of select="./areasons-cracked-trials-perc"/><xsl:text>%)</xsl:text>
							</fo:block>	
						</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								 <xsl:text>B Acceptable guilty plea(s) entered late to some or all charge / counts on the charge sheet, previously rejected by the prosecution</xsl:text> 
							</fo:block>		
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								<xsl:value-of select="./breasons-cracked-trials"/><xsl:text> (</xsl:text> <xsl:value-of select="./breasons-cracked-trials-perc"/><xsl:text>%)</xsl:text>
							</fo:block>	
						</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								 <xsl:text> C Acceptable guilty plea(s) to alternative new charge (not previously on the charge sheet), first time offered by defence </xsl:text>
							</fo:block>		
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								<xsl:value-of select="./creasons-cracked-trials"/>  <xsl:text> (</xsl:text><xsl:value-of select="./creasons-cracked-trials-perc"/> <xsl:text>%)</xsl:text>
							</fo:block>	
						</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								 <xsl:text>D Acceptable guilty plea(s) to alternative new charge (not previously on the charge sheet), previously rejected by the prosecution</xsl:text>
							</fo:block>		
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								<xsl:value-of select="./dreasons-cracked-trials"/> <xsl:text> (</xsl:text> <xsl:value-of select="./dreasons-cracked-trials-perc"/> <xsl:text>%)</xsl:text>
							</fo:block>	
						</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								 <xsl:text> E Defendant bound over, acceptable to prosecution, offered for the first time by the defence</xsl:text>
							</fo:block>		
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								<xsl:value-of select="./ereasons-cracked-trials"/> <xsl:text> (</xsl:text> <xsl:value-of select="./ereasons-cracked-trials-perc"/> <xsl:text>%)</xsl:text>
							</fo:block>	
						</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								 <xsl:text>F Defendant bound over, now acceptable to prosecution – previously rejected by prosecution</xsl:text>
							</fo:block>		
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								<xsl:value-of select="./freasons-cracked-trials"/> <xsl:text> (</xsl:text>  <xsl:value-of select="./freasons-cracked-trials-perc"/> <xsl:text>%)</xsl:text>
							</fo:block>	
						</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								 <xsl:text>G Unable to proceed with trial because defendant incapable through alcohol / drugs</xsl:text> 
							</fo:block>		
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								<xsl:value-of select="./greasons-cracked-trials"/> <xsl:text> (</xsl:text> <xsl:value-of select="./greasons-cracked-trials-perc"/> <xsl:text>%)</xsl:text>
							</fo:block>	
						</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								<xsl:text>H Defendant deceased</xsl:text>
							</fo:block>		
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								<xsl:value-of select="./hreasons-cracked-trials"/> <xsl:text> (</xsl:text> <xsl:value-of select="./hreasons-cracked-trials-perc"/> <xsl:text>%)</xsl:text>
							</fo:block>	
						</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								 <xsl:text>I Prosecution end case: insufficient evidence</xsl:text> 
							</fo:block>		
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								<xsl:value-of select="./ireasons-cracked-trials"/> <xsl:text> (</xsl:text> <xsl:value-of select="./ireasons-cracked-trials-perc"/> <xsl:text>%)</xsl:text>
							</fo:block>	
						</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								 <xsl:text>J Prosecution end case: witness absent / withdraw</xsl:text> 
							</fo:block>		
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								 <xsl:value-of select="./jreasons-cracked-trials"/> <xsl:text> (</xsl:text> <xsl:value-of select="./jreasons-cracked-trials-perc"/> <xsl:text>%)</xsl:text>
							</fo:block>	
						</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								  <xsl:text>K Prosecution end case: public interest grounds</xsl:text> 
							</fo:block>		
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								<xsl:value-of select="./kreasons-cracked-trials"/> <xsl:text> (</xsl:text> <xsl:value-of select="./kreasons-cracked-trials-perc"/> <xsl:text>%)</xsl:text>
							</fo:block>	
						</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								  <xsl:text>L Prosecution end case: adjournment refused</xsl:text> 
							</fo:block>		
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								<xsl:value-of select="./lreasons-cracked-trials"/> <xsl:text> (</xsl:text> <xsl:value-of select="./lreasons-cracked-trials-perc"/> <xsl:text>%)</xsl:text>
							</fo:block>	
						</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								  <xsl:text>CRACKED DUE TO PROSECUTION (B,D,F,I,J,K,L)</xsl:text> 
							</fo:block>		
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								<xsl:value-of select="./acracked-prosecution"/> <xsl:text> (</xsl:text> <xsl:value-of select="./acracked-prosecution-perc"/><xsl:text>%)</xsl:text>
							</fo:block>	
						</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								 <xsl:text>CRACKED DUE TO DEFENCE (A,C,E,G,H)</xsl:text> 
							</fo:block>		
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								<xsl:value-of select="./bcracked-defendant"/>  <xsl:text> (</xsl:text> <xsl:value-of select="./bcracked-defendant-perc"/> <xsl:text>%)</xsl:text>
							</fo:block>	
						</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
						<fo:table-cell display-align="after">
							<fo:block margin-top="3mm" font-size="11pt" font-family="sans-serif" font-weight="bold" text-align="left">
								<xsl:text>REASONS FOR INEFFECTIVE TRIALS</xsl:text>
							</fo:block>		
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="11pt" font-family="sans-serif" font-weight="bold" text-align="left">
								 <xsl:text>NUMBER AND PERCENTAGE IN MONTH</xsl:text>
							</fo:block>	
						</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								 <xsl:text> M1 Prosecution not ready: served late notice of additional evidence by defence</xsl:text>
							</fo:block>		
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								<xsl:value-of select="./m1-ineffective"/> <xsl:text> (</xsl:text> <xsl:value-of select="./m1-ineffective-perc"/> <xsl:text>%)</xsl:text>
							</fo:block>	
						</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								 <xsl:text>M2 Prosecution not ready: specify in comments</xsl:text>
							</fo:block>		
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								<xsl:value-of select="./m2-ineffective"/> <xsl:text> (</xsl:text> <xsl:value-of select="./m2-ineffective-perc"/> <xsl:text>%)</xsl:text>
							</fo:block>	
						</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								 <xsl:text>M3 Prosecution failed to disclose unused material</xsl:text>
							</fo:block>		
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								<xsl:value-of select="./m3-ineffective"/> <xsl:text> (</xsl:text> <xsl:value-of select="./m3-ineffective-perc"/> <xsl:text>%)</xsl:text>
							</fo:block>	
						</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								 <xsl:text>N1 Prosecution witness absent: police</xsl:text>
							</fo:block>		
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								<xsl:value-of select="./n1-ineffective"/> <xsl:text> (</xsl:text> <xsl:value-of select="./n1-ineffective-perc"/> <xsl:text>%)</xsl:text>
							</fo:block>	
						</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								 <xsl:text> N2  Prosecution witness absent: professional / expert</xsl:text>
							</fo:block>		
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								 <xsl:value-of select="./n2-ineffective"/> <xsl:text> (</xsl:text><xsl:value-of select="./n2-ineffective-perc"/> <xsl:text>%)</xsl:text>
							</fo:block>	
						</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								 <xsl:text> N3  Prosecution witness absent: other</xsl:text>
							</fo:block>		
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								 <xsl:value-of select="./n3-ineffective"/> <xsl:text> (</xsl:text><xsl:value-of select="./n3-ineffective-perc"/> <xsl:text>%)</xsl:text>
							</fo:block>	
						</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								  <xsl:text> O1  Prosecution advocate engaged in another trial</xsl:text>
							</fo:block>		
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								 <xsl:value-of select="./o1-ineffective"/> <xsl:text> (</xsl:text> <xsl:value-of select="./o1-ineffective-perc"/> <xsl:text>%)</xsl:text>
							</fo:block>	
						</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								<xsl:text> O2  Prosecution advocate failed to attend</xsl:text>
							</fo:block>		
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								<xsl:value-of select="./o2-ineffective"/> <xsl:text> (</xsl:text> <xsl:value-of select="./o2-ineffective-perc"/> <xsl:text>%)</xsl:text>
							</fo:block>	
						</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								<xsl:text> P  Prosecution increased time estimate – insufficient time for trial to start</xsl:text>
							</fo:block>		
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								<xsl:value-of select="./pineffective"/> <xsl:text> (</xsl:text> <xsl:value-of select="./pineffective-perc"/> <xsl:text>%)</xsl:text>
							</fo:block>	
						</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								 <xsl:text> Q1 Defence not ready: disclosure problems</xsl:text> 
							</fo:block>		
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								<xsl:value-of select="./q1-ineffective"/> <xsl:text> (</xsl:text> <xsl:value-of select="./q1-ineffective-perc"/> <xsl:text>%)</xsl:text>
							</fo:block>	
						</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								  <xsl:text> Q2  Defence not ready: specify in comments (inc. no instructions)</xsl:text>
							</fo:block>		
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								<xsl:value-of select="./q2-ineffective"/> <xsl:text> (</xsl:text> <xsl:value-of select="./q2-ineffective-perc"/> <xsl:text>%)</xsl:text>
							</fo:block>	
						</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								 <xsl:text> Q3 Defence asked for additional prosecution witness to attend</xsl:text>
							</fo:block>		
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								 <xsl:value-of select="./q3-ineffective"/> <xsl:text> (</xsl:text> <xsl:value-of select="./q3-ineffective-perc"/> <xsl:text>%)</xsl:text>
							</fo:block>	
						</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								<xsl:text> R  Defence witness absent</xsl:text>
							</fo:block>		
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								 <xsl:value-of select="./rineffective"/> <xsl:text> (</xsl:text> <xsl:value-of select="./rineffective-perc"/> <xsl:text>%)</xsl:text>
							</fo:block>	
						</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								 <xsl:text> S1 Defendant absent – did not proceed in absence (judicial discretion)</xsl:text>
							</fo:block>		
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								 <xsl:value-of select="./s1-ineffective"/> <xsl:text> (</xsl:text> <xsl:value-of select="./s1-ineffective-perc"/> <xsl:text>%)</xsl:text>
							</fo:block>	
						</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								 <xsl:text> S2 Defendant ill or otherwise unfit to proceed</xsl:text>
							</fo:block>		
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								 <xsl:value-of select="./s2-ineffective"/> <xsl:text> (</xsl:text> <xsl:value-of select="./s2-ineffective-perc"/> <xsl:text>%)</xsl:text>
							</fo:block>	
						</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								<xsl:text> S3 Defendant not produced by PECS</xsl:text>
							</fo:block>		
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								 <xsl:value-of select="./s3-ineffective"/> <xsl:text> (</xsl:text> <xsl:value-of select="./s3-ineffective-perc"/> <xsl:text>%)</xsl:text>
							</fo:block>	
						</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								<xsl:text> S4 Defendant absent – unable to proceed as defendant not notified of place and time of hearing</xsl:text>
							</fo:block>		
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								  <xsl:value-of select="./s4-ineffective"/> <xsl:text> (</xsl:text> <xsl:value-of select="./s4-ineffective-perc"/> <xsl:text>%)</xsl:text>
							</fo:block>	
						</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								<xsl:text> T Defence increased time estimate – insufficient time for trial to start</xsl:text>
							</fo:block>		
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								 <xsl:value-of select="./tineffective"/> <xsl:text> (</xsl:text> <xsl:value-of select="./tineffective-perc"/> <xsl:text>%)</xsl:text>
							</fo:block>	
						</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								<xsl:text> U1 Defence advocate engaged in other trial</xsl:text>
							</fo:block>		
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								 <xsl:value-of select="./u1-ineffective"/>  <xsl:text> (</xsl:text><xsl:value-of select="./u1-ineffective-perc"/> <xsl:text>%)</xsl:text>
							</fo:block>	
						</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
									<xsl:text> U2 Defence advocate failed to attend</xsl:text>
							</fo:block>		
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								  <xsl:value-of select="./u2-ineffective"/>  <xsl:text> (</xsl:text><xsl:value-of select="./u2-ineffective-perc"/> <xsl:text>%)</xsl:text>
							</fo:block>	
						</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								<xsl:text> V  Defendant dismissed advocate</xsl:text> 
							</fo:block>		
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								 <xsl:value-of select="./vineffective"/> <xsl:text> (</xsl:text> <xsl:value-of select="./vineffective-perc"/> <xsl:text>%)</xsl:text>
							</fo:block>	
						</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								<xsl:text> W1 Another case over-ran</xsl:text>
							</fo:block>		
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								  <xsl:value-of select="./w1-ineffective"/> <xsl:text> (</xsl:text> <xsl:value-of select="./w1-ineffective-perc"/> <xsl:text>%)</xsl:text>
							</fo:block>	
						</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								<xsl:text> W2 Judge / magistrate availability:</xsl:text>
							</fo:block>		
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								  <xsl:value-of select="./w2-ineffective"/> <xsl:text> (</xsl:text> <xsl:value-of select="./w2-ineffective-perc"/> <xsl:text>%)</xsl:text>
							</fo:block>	
						</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								<xsl:text> W3  Case not reached / insufficient cases dropped out / floater not reached</xsl:text>
							</fo:block>		
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								  <xsl:value-of select="./w3-ineffective"/> <xsl:text> (</xsl:text> <xsl:value-of select="./w3-ineffective-perc"/> <xsl:text>%)</xsl:text>
							</fo:block>	
						</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								<xsl:text>W4 Equipment / accommodation failure</xsl:text>
							</fo:block>		
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								 <xsl:value-of select="./w4-ineffective"/> <xsl:text> (</xsl:text> <xsl:value-of select="./w4-ineffective-perc"/> <xsl:text>%)</xsl:text>
							</fo:block>	
						</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								<xsl:text> W5  No interpreter available</xsl:text>
							</fo:block>		
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								  <xsl:value-of select="./w5-ineffective"/> <xsl:text> (</xsl:text> <xsl:value-of select="./w5-ineffective-perc"/> <xsl:text>%)</xsl:text>
							</fo:block>	
						</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								<xsl:text> X Insufficient jurors available</xsl:text>
							</fo:block>		
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								  <xsl:value-of select="./xineffective"/> <xsl:text> (</xsl:text> <xsl:value-of select="./xineffective-perc"/> <xsl:text>%)</xsl:text>
							</fo:block>	
						</fo:table-cell>
				</fo:table-row>			
				<fo:table-row>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								<xsl:text> Y Outstanding committals in a magistrates’ court</xsl:text>
							</fo:block>		
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								  <xsl:value-of select="./yineffective"/> <xsl:text> (</xsl:text> <xsl:value-of select="./yineffective-perc"/> <xsl:text>%)</xsl:text>
							</fo:block>	
						</fo:table-cell>
				</fo:table-row>
				<fo:table-row>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								<xsl:text> Z Outstanding committals in another Crown Court centre </xsl:text>
							</fo:block>		
						</fo:table-cell>
						<fo:table-cell>
							<fo:block  margin-top="3mm" font-size="10pt" font-family="sans-serif" font-weight="normal" text-align="left">
								  <xsl:value-of select="./zineffective"/> <xsl:text> (</xsl:text> <xsl:value-of select="./zineffective-perc"/> <xsl:text>%)</xsl:text>
							</fo:block>	
						</fo:table-cell>
				</fo:table-row>			
			</fo:table-body>
		</fo:table>

</xsl:for-each>					
</xsl:template>
</xsl:stylesheet>	