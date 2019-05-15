%MACRO time (when);
	%IF %UPCASE(&when)=START %THEN
		%DO;
			%GLOBAL start;
			%LET start=%SYSFUNC(TIME());
		%END;

	%IF %UPCASE(&when)=END %THEN
		%DO;
			%PUT Total time elapsed : %SYSFUNC(ROUND(%SYSFUNC(TIME())-&start,.1),TIME12.1) [hh:mm:ss];
		%END;
%MEND time;


%time(START);
proc printto
log="&output\log_analyse.log" new;
run;

%include "&pathSASMacros\RUN_LUMPSUM.sas";
%include "&pathSASMacros\ended_status.sas"; 

%go_run(run_id=&id
,input_PATH=&input
,output_PATH=&output
,SASMacros=&pathSASMacros
,drop_dimension=&dropdim
,Data_structure_type=&type
,RCLC_ID=&rclcid
,LIB_ENV=&libEnv
,URL_STATUS=&hostName
);

%time(END);

proc printto;
run;

%ended_status(output_PATH=&output
,RCLC_ID=&rclcid
,LIB_ENV=&libEnv
,URL_STATUS=&hostName);