/*===========================================================================
*
*                            PUBLIC DOMAIN NOTICE
*               National Center for Biotechnology Information
*
*  This software/database is a "United States Government Work" under the
*  terms of the United States Copyright Act.  It was written as part of
*  the author's official duties as a United States Government employee and
*  thus cannot be copyrighted.  This software/database is freely available
*  to the public for use. The National Library of Medicine and the U.S.
*  Government have not placed any restriction on its use or reproduction.
*
*  Although all reasonable efforts have been taken to ensure the accuracy
*  and reliability of the software and data, the NLM and the U.S.
*  Government do not and cannot warrant the performance or results that
*  may be obtained by using this software or data. The NLM and the U.S.
*  Government disclaim all warranties, express or implied, including
*  warranties of performance, merchantability or fitness for any particular
*  purpose.
*
*  Please cite the author in any work or product based on this material.
*
* ===========================================================================
*
*/

package gov.nih.nlm.ncbi.blast_spark_cluster;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class BC_DATABASE_RDD_ENTRY implements Serializable
{
    public final BC_DATABASE_SETTING setting;
    public final String name;

    public BC_DATABASE_RDD_ENTRY( final BC_DATABASE_SETTING a_setting, final String a_name )
    {
		setting = a_setting;
		name = a_name;
    }

	public static List< BC_DATABASE_RDD_ENTRY > make_rdd_entry_list( final BC_DATABASE_SETTING a_setting, final List< String > names )
	{
		List< BC_DATABASE_RDD_ENTRY > res = new ArrayList<>();
		for ( String a_name : names )
			res.add( new BC_DATABASE_RDD_ENTRY( a_setting, a_name ) );
		return res;
	}
}
