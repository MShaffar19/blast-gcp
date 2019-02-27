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
package gov.nih.nlm.ncbi.blastjni;

import java.io.Serializable;

// Warning: C++ JNI code expects this to be named BLAST_TB_LIST
class BLAST_TB_LIST implements Serializable, Comparable<BLAST_TB_LIST> {
  private static final double EPSILON = 1.0e-6;

  private int oid;
  private double evalue;
  public byte[] asn1_blob;
  public int top_n;

  BLAST_TB_LIST(final int oid, final double evalue, final byte[] asn1_blob) {
    this.oid = oid;
    this.evalue = evalue;
    this.asn1_blob = asn1_blob;
  }

  public Boolean isEmpty() {
    return asn1_blob.length == 0;
  }

  /* recommended by BLAST-team */
  static int FuzzyEvalueComp(final double evalue1, final double evalue2) {
    if (evalue1 < (1.0 - EPSILON) * evalue2) {
      return -1;
    } else if (evalue1 > (1.0 + EPSILON) * evalue2) {
      return 1;
    } else {
      return 0;
    }
  }

  /*
      0  ... equal
      -1 ... this > other
      +1 ... this < other
  */
  @Override
  public int compareTo(final BLAST_TB_LIST other) {
    // ascending order
    final double delta = this.evalue - other.evalue;
    if (delta < EPSILON && delta > -EPSILON) {
      final int oid_delta = this.oid - other.oid;
      if (oid_delta == 0) {
        return 0;
      } else if (oid_delta > 0) {
        return 1;
      } else {
        return -1;
      }
    } else if (delta > EPSILON) {
      return 1;
    } else {
      return -1;
    }
  }

  public static String toHex(byte[] blob) {
    String res = "";
    res += "\n        ";
    int brk = 0;
    for (final byte b : blob) {
      res += String.format("%02x", b);
      ++brk;
      if (brk % 4 == 0) {
        res += " ";
      }
      if (brk % 32 == 0) {
        res += "\n        ";
      }
    }
    res += "\n";
    return res;
  }

  @Override
  public String toString() {
    String res = "  TBLIST";
    res += String.format("\n  evalue=%f oid=%d", evalue, oid);
    if (asn1_blob != null) {
      res += "\n  " + asn1_blob.length + " bytes in ASN.1 blob\n";
    }
    if (asn1_blob != null) {
      res += toHex(asn1_blob);
    }
    return res;
  }
}
