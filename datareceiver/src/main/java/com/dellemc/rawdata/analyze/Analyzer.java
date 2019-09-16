package com.dellemc.rawdata.analyze;

import java.sql.SQLException;

public interface Analyzer {
    /**
     * 分析每条message的内容
     * @param message
     *  515963.594: GenCollectForAllocation          [    2778          0              1    ]      [     0     0     0    56   196    ]  0
     *          vmop                    [threads: total initially_running wait_to_block]    [time: spin block sync cleanup vmop] page_trap_count
     * 515966.562: GenCollectForAllocation          [    2777          0              0    ]      [     0     0     1    97   116    ]  0
     *          vmop                    [threads: total initially_running wait_to_block]    [time: spin block sync cleanup vmop] page_trap_count
     * 515969.438: GenCollectForAllocation          [    2777          0              0    ]      [     0     0     0    80    85    ]  0
     *          vmop                    [threads: total initially_running wait_to_block]    [time: spin block sync cleanup vmop] page_trap_count
     * 515971.844: GenCollectForAllocation          [    2777          0              0    ]      [     0     0     0   189   190    ]  0
     *          vmop                    [threads: total initially_running wait_to_block]    [time: spin block sync cleanup vmop] page_trap_count
     * 515974.938: GenCollectForAllocation          [    2777          0              1    ]      [     0     0     0    65   294    ]  0
     *          vmop                    [threads: total initially_running wait_to_block]    [time: spin block sync cleanup vmop] page_trap_count
     * 515977.906: GenCollectForAllocation          [    2777          0              0    ]      [     0     0     1    81   430    ]  0
     *          vmop                    [threads: total initially_running wait_to_block]    [time: spin block sync cleanup vmop] page_trap_count
     * 515981.375: GenCollectForAllocation          [    2777          0              0    ]      [     0     0     1   152   226    ]  0
     *          vmop                    [threads: total initially_running wait_to_block]    [time: spin block sync cleanup vmop] page_trap_count
     * 515986.375: GenCollectForAllocation          [    2777          0              0    ]      [     0     0     0    60    59    ]  0
     *          vmop                    [threads: total initially_running wait _to_block]    [time: spin block sync cleanup vmop] page_trap_count
     * 515991.312: GenCollectForAllocation          [    2777          0              1    ]      [     0     0     1    63    65    ]  0
     *          vmop                    [threads: total initially_running wait_to_block]    [time: spin block sync cleanup vmop] page_trap_count
     */
    void analyze(String message);
    void close() throws SQLException, Exception;
}
