/*
 * @(#CoinDao.java 05/10/2015
 * Copyright 2015 bitDubai, Inc. All rights reserved.
 * BITDUBAI/CONFIDENTIAL
 * */

package com.bitdubai.fermat_dmp_plugin.layer.middleware.bank_notes.developer.bitdubai.version_1.interfaces.dao;


// Packages and classes to import of Middleware Bank Notes API.
import com.bitdubai.fermat_dmp_plugin.layer.middleware.bank_notes.developer.bitdubai.version_1.beans.dto.Coin;


/**
 *
 *  <p>The abstract class <code>com.bitdubai.fermat_dmp_plugin.layer._14_middleware.bank_notes.developer.bitdubai.version_1.interfaces.dao.CoinDao</code> is a interface
 *     that define the methods for management the CRUD operations for the Coin DTO.
 *
 *
 *  @author  Raul Geomar Pena (raul.pena@mac.com)
 *  @version 1.0.0
 *  @since   jdk 1.7
 *  @since   05/10/2015
 *  @see     {@link com.bitdubai.fermat_dmp_plugin.layer.middleware.bank_notes.developer.bitdubai.version_1.interfaces.dao.Dao}
 *  @see     {@link com.bitdubai.fermat_dmp_plugin.layer.middleware.bank_notes.developer.bitdubai.version_1.beans.dto.Coin}
 * */
public interface CoinDao<I, O> extends Dao<Long, Coin> {


    // Public instance methods declarations.
}