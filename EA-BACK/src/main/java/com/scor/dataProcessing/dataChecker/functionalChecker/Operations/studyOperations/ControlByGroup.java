package com.scor.dataProcessing.dataChecker.functionalChecker.Operations.studyOperations;

import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.util.LongAccumulator;

import com.scor.dataProcessing.Helpers.Headers;
import com.scor.dataProcessing.accumulators.ControlResultAccumulator;
import com.scor.dataProcessing.models.AffectedColumn;
import com.scor.dataProcessing.models.ControlResult;

import scala.Tuple2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ControlByGroup implements VoidFunction<Tuple2<String, Iterable<String[]>>> {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1147167195781044020L;
	private ControlResultAccumulator cc_acc;
    private ControlResultAccumulator overlap_acc;
    private ControlResultAccumulator NoStatusDateSpace;
    private String type;
    private List<String> names;
    private LongAccumulator errorsCount;

    public ControlByGroup(ControlResultAccumulator ac, ControlResultAccumulator overlap_acc,ControlResultAccumulator NoStatusDateSpace, String type, List<String> names, LongAccumulator errorsCount){
        this.cc_acc = ac;
        this.type = type;
        this.names = names;
        this.overlap_acc = overlap_acc;
        this.NoStatusDateSpace = NoStatusDateSpace;
        this.errorsCount = errorsCount;
    }

    @Override
    public void call(Tuple2<String, Iterable<String[]>> row) throws Exception {
      //  List<String[]> data = StreamSupport.stream(row._2.spliterator(), false)
      //          .collect(Collectors.toList());
        //System.out.println("key ----------------> "+row._1);
        String previous_line = null;
        String previous_end_date = null;
        Boolean canExecute3 = canExecuteControl_3(names);
        Boolean canExecute12AndNoStatusDateSpace = canExecuteControl_12_NoStatusDateSpace(names);

        for (String[] line : row._2)  {
            String begin_stat = line[names.indexOf(Headers.STATUS_BEGIN_CURRENT_CONDITION)];
            String end_stat;

            // Controle 3
            if (canExecute3) {
                end_stat = line[names.indexOf(Headers.STATUS_END_CURRENT_CONDITION)];
                if (control_3(begin_stat, previous_line)) {
                    if ((type.equalsIgnoreCase(Headers.COMBINE)
                            || (type.equalsIgnoreCase(Headers.SPLIT) && names.contains(Headers.EXPOSURE_OR_EVENT)
                            && ("exposure + event"
                            .equalsIgnoreCase(line[names.indexOf(Headers.EXPOSURE_OR_EVENT)])
                            || "exposure".equalsIgnoreCase(
                            line[names.indexOf(Headers.EXPOSURE_OR_EVENT)]))))) {
                        List<AffectedColumn> affectedColumns = new ArrayList<>();
                        affectedColumns.add(new AffectedColumn(
                                "status at begin current condition & status at end current condition",
                                1, new ArrayList<>(Arrays.asList(String.join(";", line)))));
                        cc_acc.add(new ControlResult("Status Consistency Check", affectedColumns));
                        errorsCount.add(1);
                    }
                }
                previous_line = end_stat;
            }

            // Controle 12
            if (canExecute12AndNoStatusDateSpace) {
                if (control_12(line, previous_end_date, names)) {
                    if ((type.equalsIgnoreCase(Headers.COMBINE)
                            || (type.equalsIgnoreCase(Headers.SPLIT) &&names.contains(Headers.EXPOSURE_OR_EVENT) && ("exposure + event"
                            .equalsIgnoreCase(line[names.indexOf(Headers.EXPOSURE_OR_EVENT)])
                            || "exposure".equalsIgnoreCase(line[names.indexOf(Headers.EXPOSURE_OR_EVENT)]))))) {
                        List<AffectedColumn> affectedColumns = new ArrayList<>();
                        affectedColumns.add(new AffectedColumn("overlap check", 1,
                                new ArrayList<>(Arrays.asList(String.join(";", line)))));
                        overlap_acc.add(new ControlResult("Overlap Check", affectedColumns));
                        errorsCount.add(1);
                    }

                }

                if(control_NoStatusDateSpace(line,previous_end_date,names)){
                    List<AffectedColumn> affectedColumns = new ArrayList<>();
                    affectedColumns.add(new AffectedColumn("status date space", 1,
                            new ArrayList<>(Arrays.asList(String.join(";", line)))));
                    NoStatusDateSpace.add(new ControlResult("status date space", affectedColumns));
                    errorsCount.add(1);
                }

                previous_end_date = line[names.indexOf(Headers.DATE_OF_END_CURRENT_CONDITION)];
                System.out.println(previous_end_date);

            }



        }

    }

    private boolean control_3(String begin_stat, String previous_end_state) {
        if (previous_end_state != null) {
            if (!previous_end_state.equalsIgnoreCase(begin_stat)) {
                return true;
            }
        }
        return false;
    }

    private boolean canExecuteControl_3(List<String> cols) {
        return (cols.containsAll(Arrays.asList(Headers.STATUS_END_CURRENT_CONDITION)));
    }

    private boolean canExecuteControl_12_NoStatusDateSpace(List<String> cols) {
        return cols.containsAll(Arrays.asList(Headers.DATE_OF_END_CURRENT_CONDITION));
    }


    private boolean control_12(String[] line, String previous_end_date, List<String> cols) throws ParseException {
        String date_begin = line[cols.indexOf(Headers.DATE_OF_BEGIN_CURRENT_CONDITION)];
        Date date_begin_current_line = null;
        Date date_end_previous_row = null;
        if (previous_end_date != null && previous_end_date.matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$")) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            date_end_previous_row = formatter.parse(previous_end_date);
        }
        if (date_begin.matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$")) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            date_begin_current_line = formatter.parse(date_begin);
        }
        if (date_begin_current_line != null && date_end_previous_row != null
                && (date_end_previous_row.equals(date_begin_current_line)
                || date_end_previous_row.after(date_begin_current_line))) {
            //System.out.println(previous_end_date + " < " + date_begin);
            return true;
        }
        return false;
    }
    private boolean control_NoStatusDateSpace(String[] line, String previous_end_date, List<String> cols) throws ParseException {
        String date_begin = line[cols.indexOf(Headers.DATE_OF_BEGIN_CURRENT_CONDITION)];
        Date date_begin_current_line = null;
        Date date_end_previous_row = null;
        if (previous_end_date != null && previous_end_date.matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$")) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            date_end_previous_row = formatter.parse(previous_end_date);
        }
        if (date_begin.matches("^[0-9]{2}/[0-9]{2}/[0-9]{4}$")) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            date_begin_current_line = formatter.parse(date_begin);
        }
        if (date_begin_current_line != null && date_end_previous_row != null) {
            long diffInMillies = Math.abs(date_begin_current_line.getTime() - date_end_previous_row.getTime());
            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                if(diff != 1) return true;
        }
        return false;
    }

}
