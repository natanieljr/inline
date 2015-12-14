From email of Philipp sent to me at 11 Aug 2015 20:19 to jamrozik@st.cs.uni-saarland.de

The email is empty, I guess the context was lost in my meeting with Philipp.

My manual investigation on 9 Oct 2015 seems to show that this is a definition of a monitor.java that can interface both with old dalvik
and new ART VM, plus the Instrumentation class definition that bridges to native instrumentation. 

Seems that I use some older version of this instrumentation in MonitorJavaTemplate. 
I use 
de.uds.infsec.instrumentation.Instrumentation 
while this project has 
com.srt.appguard.monitor.instrumentation.Instrumentation.

The instrumentation of this project seems to have been used to obtain the current appguard apis, as can be seen in
com.srt.appguard.monitor.MonitorInitalizer.