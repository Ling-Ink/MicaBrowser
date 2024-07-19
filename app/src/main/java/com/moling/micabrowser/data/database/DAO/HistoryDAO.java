package com.moling.micabrowser.data.database.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.moling.micabrowser.data.database.Entities.History;

import java.util.List;

@Dao
public interface HistoryDAO {
    /**
     * 查询所有浏览历史，返回 List 集合
     */
    @Query("SELECT * FROM histories ORDER BY timeStamp")
    List<History> getAllHistories();
    /**
     * 插入数据，onConflict = OnConflictStrategy.REPLACE表明若存在主键相同的情况则直接覆盖
     * 返回的long表示的是插入项新的id
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertHistory(History history);

    /**
     * 更新数据，这意味着可以指定id然后传入新的person对象进行更新
     * 返回的long表示更新的行数
     */
    @Update
    int updateHistory(History history);

    /**
     * 删除数据，根据传入实体的主键进行数据的删除。
     * 也可以返回long型数据，表明从数据库中删除的行数
     */
    @Delete
    int deleteHistory(History history);
}
