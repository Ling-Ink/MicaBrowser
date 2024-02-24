package com.moling.micabrowser.data.database.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.moling.micabrowser.data.database.Entities.Download;

import java.util.List;

@Dao
public interface DownloadDAO {
    /**
     * 查询所有下载项，返回 List 集合
     */
    @Query("SELECT * FROM downloads")
    List<Download> getAllDownloads();
    /**
     * 插入数据，onConflict = OnConflictStrategy.REPLACE表明若存在主键相同的情况则直接覆盖
     * 返回的long表示的是插入项新的id
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertDownload(Download download);

    /**
     * 更新数据，这意味着可以指定id然后传入新的person对象进行更新
     * 返回的long表示更新的行数
     */
    @Update
    int updateDownload(Download download);

    /**
     * 删除数据，根据传入实体的主键进行数据的删除。
     * 也可以返回long型数据，表明从数据库中删除的行数
     */
    @Delete
    int deleteDownload(Download download);
}
